# P0–P3 production-readiness & code-quality design

**Date:** 2026-05-02
**Status:** approved (in-chat)
**Author:** Claude (under Udit Goel's direction)

## Goal

Close the gaps surfaced in the README's *Caveats & known follow-ups* section so Groomify can sit
behind a load balancer with confidence: real schema migrations, observability, refresh-token
rotation, input validation, plus the code-quality and tooling cleanup that supports them.

## Non-goals

- Multi-tenant or multi-region deployment
- Account-level lockout / 2FA / TOTP (could be added later in a separate spec)
- Distributed rate limiting (multi-instance) — flagged in caveats but out of scope here
- Replacing Maven with Gradle, Java 17 → 21, Spring Boot 3.2 → 3.4 (separate scope)
- IDE / editor preferences, README design (already done)

## Scope — 16 items across 4 priority buckets

### P0 — production-readiness blockers

| # | Item | Why |
|---|---|---|
| 1 | Flyway migrations; replace `ddl-auto=update` with `validate` in prod | Auto-DDL is silent schema drift; no rollback story |
| 2 | Spring Boot Actuator + Prometheus + JSON-structured logging | k8s probes, JVM/HTTP metrics, ELK-compatible logs |
| 3 | Refresh-token rotation + reuse detection | Today refresh tokens are reusable indefinitely until 60-day expiry |
| 4 | Bean Validation on every input DTO + `MethodArgumentNotValidException` → 400 | Defence in depth; clearer 400s |

### P1 — code quality

| # | Item | Why |
|---|---|---|
| 5 | `AppUtils` static-state → real `@Component` instance | Removes `@SuppressWarnings("java:S2696")` and static-init fragility |
| 6 | JPA entities `Date`/`Calendar` → `java.time.LocalDate`/`Instant` | Modern API, cleaner code, fewer `Calendar.getInstance()` glue calls |
| 7 | Consolidate `Custom{Customer,Employee,User}DetailsService` + twin `*AuthController` | ~600 LOC delete; one strategy, parameterised paths |
| 8 | Convert remaining 23 DTOs to records | ~30 LOC saved per file, immutable by default |

### P2 — API quality

| # | Item | Why |
|---|---|---|
| 9 | `Pageable` on list endpoints | Unbounded lists will OOM on a real customer base |
| 10 | OpenAPI annotations on every controller | Swagger UI is currently sparse |
| 11 | RFC 7807 `application/problem+json` (Spring 6 `ProblemDetail`) | Standard error shape; better tooling integration |
| 12 | `@Cacheable` on reference-data repositories (Caffeine, 1-hour TTL) | Free signup p95 win |

### P3 — tooling / CI

| # | Item | Why |
|---|---|---|
| 13 | `.github/dependabot.yml` (maven + github-actions, weekly) | Automated dep updates |
| 14 | JaCoCo coverage report (soft-gated initially) | Visibility before enforcement |
| 15 | OWASP `dependency-check-maven` bound to `verify` | CVE flagging on every PR |
| 16 | Spotless + Google Java Format | Uniform formatting; eliminate review nits |

## Branching & PR strategy

**One branch per priority bucket; four PRs total**, merged sequentially:

| PR | Branch | Items |
|---|---|---|
| 1 | `chore/p0-prod-readiness` | #1 #2 #3 #4 |
| 2 | `chore/p1-code-quality` | #5 #6 #7 #8 |
| 3 | `chore/p2-api-quality` | #9 #10 #11 #12 |
| 4 | `chore/p3-tooling` | #13 #14 #15 #16 |

Each PR rebases on the previous after merge. Total ~3 days of focused work; ~16 commits across
the 4 PRs.

## Detailed design — P0

### 1. Flyway migrations

- Add deps: `org.flywaydb:flyway-core`, `org.flywaydb:flyway-database-postgresql`.
- Generate baseline by running the app once with the existing Hibernate `update` mode against an
  empty DB, then `pg_dump --schema-only` → hand-curate into `src/main/resources/db/migration/V1__baseline.sql`.
  Strip the `flyway_schema_history` table (Flyway creates it itself).
- Set in `application.properties`:
  - `spring.flyway.enabled=true`
  - `spring.flyway.baseline-on-migrate=true` (so existing dev volumes don't need wiping)
  - `spring.flyway.locations=classpath:db/migration`
- Set `spring.jpa.hibernate.ddl-auto=validate` (currently `update`).
- Test profile: `spring.flyway.enabled=false`, `spring.jpa.hibernate.ddl-auto=create-drop`. Tests
  use H2 which doesn't need Flyway.
- New e2e test: `FlywayMigrationTest` boots the app against a real Postgres (Testcontainers
  alongside the existing embedded-redis harness — single test class, conditionally skipped if
  Docker daemon is missing) and asserts `flyway_schema_history` has at least one row with
  `success=true`.

### 2. Actuator + Prometheus + structured logging

- Add deps: `spring-boot-starter-actuator`, `io.micrometer:micrometer-registry-prometheus`,
  `net.logstash.logback:logstash-logback-encoder`.
- Properties:
  - `management.endpoints.web.exposure.include=health,info,prometheus`
  - `management.endpoint.health.probes.enabled=true` (k8s liveness/readiness)
  - `management.endpoint.health.show-details=when-authorized`
  - `management.metrics.tags.application=groomify`
- `SecurityConfig` permits `/actuator/health`, `/actuator/health/**`, `/actuator/info`. The
  `/actuator/prometheus` endpoint is permitAll only on the actuator port (we won't bind a
  separate actuator port for simplicity — accept that prometheus is reachable; doc this).
- Replace `logging.pattern.console`/`logging.pattern.file` with a `logback-spring.xml` that uses
  `LogstashEncoder` for production profile, plain text for dev.
- New e2e test: `ActuatorHealthTest` hits `/actuator/health` and asserts 200 + body contains
  `"status":"UP"`.

### 3. Refresh-token rotation + reuse detection

Standard rotation-with-theft-detection pattern:

- **Token state in Redis** (one key per token, value is JSON):
  ```json
  { "username": "...", "userType": "...", "familyId": "<uuid>", "rotatedAt": null | "<iso8601>" }
  ```
  Key: `TokenType/[refresh]/Token/[<ct>]` (existing scheme; expanded value).
- **Family set**: `refresh:family:<familyId>` — Redis Set holding all `<ct>` issued in this
  family (lets us wipe the whole tree atomically).
- **At signin** (`generateToken`): generate `familyId = UUID.randomUUID()`. Store the new token
  with `rotatedAt=null`. `SADD refresh:family:<familyId> <ct>`.
- **At `/refreshToken`**:
  - `GET refresh:token:<ct>` → JSON record.
  - **Miss**: 401 — token unknown (bogus, expired, or a previously-rotated parent whose own
    rotation chain has been wiped). We can't distinguish bogus from stolen-and-already-wiped, so
    we don't try.
  - **Hit, `rotatedAt != null`**: this is **token reuse — theft signal**. Pull `familyId` from
    the record. `SMEMBERS refresh:family:<familyId>`, `DEL` each token key, `DEL` the family
    set. Return 401.
  - **Hit, `rotatedAt == null`** (first use of this token):
    - Mark the parent: `SET refresh:token:<old_ct>` with `rotatedAt = now`, retain TTL.
    - Mint new refresh token, generate new `<new_ct>`. Store with same `familyId`, `rotatedAt=null`.
    - `SADD refresh:family:<familyId> <new_ct>`.
    - Mint new access token. Return both.
- **`RedisHelper.wipeFamily(String familyId)`** — `SMEMBERS` + iterate `DEL`s + `DEL` the set
  itself.
- **Backward compat**: refresh tokens issued *before* this change have JSON values in the older
  shape (no `familyId`, no `rotatedAt`). On first refresh-call:
  - Treat missing `rotatedAt` field as `null` (allow the call).
  - Treat missing `familyId` as a fresh family of one — generate a new family on rotation.
  - This means old tokens get one final use, then they're rotated into the new scheme.
- **New e2e tests** (run on the H2 + embedded-redis harness):
  - `refreshToken_rotates_oldOneRejected`: refresh once with `T1` (gets `T2` back), then re-call
    refreshToken with `T1` → 401.
  - `refreshTokenReuse_wipesEntireFamily`: customer signs in (family `F` with `T1`), refreshes
    (`T1` rotates to `T2`, both in `F`), reuses `T1` → both `T1` and `T2` wiped → subsequent
    `T2` call returns 401.

### 4. Bean Validation on input DTOs

- Add `@Valid` to every `@RequestBody` parameter in controllers.
- Add field-level annotations to all input DTOs:
  - `CustomerDTO`: `@NotBlank` username/email/name/regId/govtId/govtIdType, `@Email` email,
    `@Pattern` for password (against existing regex), `@Size(min=8,max=16)` password,
    `@Past @NotNull` dob, `@Min @Max` contact (10-digit Indian numbers).
  - `EmployeeInput`: same plus `@Min(0)` salary, `@Past @NotNull` joiningDate, `@NotNull` roleName.
  - `LoginRequest`: already done.
  - `CreateCustomerOrderInput`, `UpdateCustomerOrderInput`: `@NotBlank` username, `@NotEmpty`
    on the service-name lists.
- Map `MethodArgumentNotValidException` in `GlobalExceptionHandler`:
  - 400 with body `{"timestamp", "status", "error":"Bad Request", "message":"validation failed",
    "path", "errors":[{"field", "rejectedValue", "message"}]}`.
- New e2e test: `signupWithMalformedPayload_returns400_withFieldErrors` — POST a customer with
  blank email and invalid contact → assert 400 + the body contains both field names in errors[].

## Detailed design — P1

### 5. `AppUtils` static-state → instance

- Make `aesKey` and the four password-expiry fields instance fields.
- Convert `encrypt`/`decrypt`/`getCustomerExpirationDateFromCurrent`/`getEmployeeExpirationDateFromCurrent`
  to instance methods.
- Inject `AppUtils` into `JwtTokenProvider`, `CustomerFacadeImpl`, `EmployeeFacadeImpl` via
  constructor injection. Remove the static API.
- Remove `@SuppressWarnings("java:S2696")` and the comment about intentional static-init.

### 6. `java.time` entity migration

- Audit pass: 22 files import `Date`/`Calendar` in `dao/`. For each entity field of `Date`:
  - `Date dob` (Customer, Employee) → `LocalDate`
  - `Date creationDate`, `lastModificationDate` (AuditInfo) → `Instant`
  - `Date jobStartTime`, `jobEndTime`, `appointmentDate` etc. → `Instant`
- Update `AuditingConfig` / `SpringSecurityAuditAwareImpl` if needed (Spring Data auditing
  works with `Instant` natively).
- Update Flyway baseline columns: `DATE` for `dob`, `TIMESTAMP WITH TIME ZONE` for the rest.
- Update `Converter.java`: `convertLocalDateToDate`, `convertDateToStartOfDay`,
  `localTimeToDate` either become no-ops (callers use `LocalDate`/`Instant` directly) or are
  retained for legacy callers we can't yet remove.
- Audit repository methods: any `Date` parameter is converted to `LocalDate`/`Instant`.

### 7. Auth controller / user-details consolidation

- New `AuthController` at `/api/auth/{userType:customer|employee}`:
  - `POST /signup` — accepts `CustomerDTO` or `EmployeeInput` based on path, dispatches via
    `Map<UserType, SignupHandler>` strategy.
  - `POST /signin` — same as today but parameterised on `userType`.
  - `POST /refreshToken` — same.
- Old paths (`/api/auth/customer/*`, `/api/auth/employee/*`) preserved via `@Deprecated`
  delegate methods so the seed script + tests don't break.
- Delete `CustomCustomerDetailsService.java`, `CustomEmployeeDetailsService.java`. The
  `Map<UserType, UserDetailsService>` already exists in `CustomUserDetailsService`; the two
  delegates become inline lambdas or trivial implementations of `UserDetailsService` that wrap
  `CustomerRepository`/`EmployeeRepository`.

### 8. DTO records

- 23 DTO classes to convert. Mechanical except for ones with downstream mutation (none we know
  of — everything goes through Jackson on input and returns out via Spring MVC).
- Bean Validation annotations work on records (Hibernate Validator 7+).

## Detailed design — P2

### 9. Pageable

- Repos: `findAllMatchingFirstName`, `findAllMatchingFNameLName`, `findAllMatchingName`,
  `fetchAllCustomerEnquiries`, `findAllByDobBetween*` → return `Page<T>`.
- Controllers: accept `Pageable` (Spring resolves from `?page=&size=&sort=`). Default
  `@PageableDefault(size=20)`.
- Response wraps in Spring's `Page` JSON shape (content / totalElements / totalPages / etc.).

### 10. OpenAPI annotations

- Class-level `@Tag(name, description)` on each controller.
- `@Operation(summary, description)` on each endpoint.
- `@ApiResponse(responseCode, description, content)` for 400 / 401 / 403 / 404 / 409 cases.
- `@Parameter` on path/query parameters with descriptions.
- Examples on signup/signin payloads.

### 11. RFC 7807 errors

- Replace `GlobalExceptionHandler`'s custom `Map<String, Object>` body with
  `org.springframework.http.ProblemDetail`.
- Set `Content-Type: application/problem+json` automatically (ProblemDetail does this).
- Bean Validation 400 responses include a custom `errors` extension property.

### 12. `@Cacheable`

- Add deps: `spring-boot-starter-cache`, `com.github.ben-manes.caffeine:caffeine`.
- `@EnableCaching` on `GroomifyApplication`.
- `@Cacheable("govtIdTypes")` on `GovtIdTypeRepository.findByTypeNameOrderByTypeNameAsc`.
- `@Cacheable("roles")` on `RoleRepository.findByName`.
- Caffeine config: 1-hour TTL, max 100 entries each.
- `@CacheEvict` on the rare admin-write paths (probably none yet, but document for future).

## Detailed design — P3

### 13. Dependabot

```yaml
# .github/dependabot.yml
version: 2
updates:
  - package-ecosystem: "maven"
    directory: "/"
    schedule: { interval: "weekly" }
    open-pull-requests-limit: 5
  - package-ecosystem: "github-actions"
    directory: "/"
    schedule: { interval: "weekly" }
```

### 14. JaCoCo

- `org.jacoco:jacoco-maven-plugin` bound to `prepare-agent` + `report` goals.
- Initial coverage rule: 0% (informational); ratchet up later.
- HTML report in `target/site/jacoco/`.
- Optional: GitHub Actions step to upload coverage artefact.

### 15. OWASP dependency-check

- `org.owasp:dependency-check-maven` bound to `verify`.
- Configure `failBuildOnCVSS=8.0` (high-severity only initially).
- Suppression file `.dependency-check-suppressions.xml` for false positives.

### 16. Spotless

- `com.diffplug.spotless:spotless-maven-plugin` with `googleJavaFormat` (latest).
- Bound to `verify` (check), `mvn spotless:apply` to format.
- First run will reformat the entire codebase — large mechanical diff, all in one commit.

## Test strategy

- **Existing suite**: 15 tests must stay green throughout. Each PR runs `mvn test` before merge.
- **New tests** (4, all e2e):
  1. `FlywayMigrationTest` — Flyway runs on Postgres, history table populated.
  2. `ActuatorHealthTest` — `/actuator/health` returns 200 UP.
  3. `RefreshTokenRotationTest` — old refresh token rejected; reuse wipes all tokens.
  4. `BeanValidationTest` — malformed signup returns 400 with field errors.
- **Final suite size**: ~19 tests, ~10 s on the H2 + embedded-redis harness; the Flyway test
  needs a Postgres container so it skips when Docker isn't available.

## Risk register

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| Flyway baseline drifts from Hibernate-generated schema | Medium | High (boot fails on `validate`) | Compare baseline.sql against fresh Hibernate output in a CI job; review diff manually before merge |
| Refresh-token rotation breaks active sessions | Certain | Low (5 demo users) | Document in PR; user has been notified |
| `java.time` query parameter mismatch | Medium | Medium (runtime errors on date-bound queries) | Audit every `?Param` in repository; integration test for each |
| Spotless reformats everything | Certain | Low (one-time noise) | Land Spotless in its own commit; subsequent diffs are clean |
| Caffeine cache invalidation stale | Low | Low (1-hour TTL bounds it) | Document in caveats; add `@CacheEvict` to admin write paths |
| OpenAPI annotations are tedious | Certain | Low (cosmetic) | Apply uniformly; if too noisy, reduce to `@Tag` + `@Operation` only and skip per-response annotations |

## Effort estimate

- P0: ~1 day (Flyway + Actuator + refresh rotation + Bean Validation; 4 new tests)
- P1: ~1 day (mostly mechanical; java.time migration is the longest)
- P2: ~half day (Pageable + OpenAPI + ProblemDetail + Caffeine)
- P3: ~2-3 hours (config files; one-time Spotless reformat)

**Total: ~3 days, 4 PRs, ~16 commits, +4 tests.**

## Out of scope (confirmed deferrals)

- Account lockout / 2FA / TOTP
- Distributed rate limiter (multi-instance Redis bucket)
- API versioning (`/api/v1/`) — defer until contracts stabilise
- Java 17 → 21 / Spring Boot 3.2 → 3.4
- Lombok adoption — records cover most of the boilerplate use case
