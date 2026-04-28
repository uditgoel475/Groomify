# Groomify

Salon booking backend — JWT-with-refresh auth, role-based access for customers and employees, full
Postgres + Redis stack, Dockerised, with end-to-end tests that don't need Docker. Originally a 2019
learning project; rebuilt as a reference for stateless-access-token + Redis-backed-refresh-token
patterns on a layered Spring Boot service.

> **Reference, not prod.** No multi-tenant, no rate limiting, no observability. Single instance,
> single tenant. Treat it as a clean implementation of the patterns, not a deployable product.

---

## Stack

- **Java 17**, **Spring Boot 3.2** (JAR packaging — embedded Tomcat 10)
- **Spring Security 6** — `SecurityFilterChain` bean, method security, custom JSON entry point
- **Spring Data JPA** / Hibernate 6 — **PostgreSQL 16** in prod, **H2** in tests
- **Spring Data Redis** (Lettuce) — refresh-token store only; access tokens are stateless
- **jjwt 0.12** — HS512 access tokens; AES-GCM for the subject payload
- **springdoc-openapi 2.3** — Swagger UI at `/swagger-ui.html`, JSON at `/v3/api-docs`
- **WebClient** (WebFlux) — geonames postal-code lookup
- Maven, Docker, GitHub Actions (gitleaks)

## Module layout

```
com.uditgoel.groomify
├── controller/        REST entry points (8 controllers, ~50 endpoints)
├── facade/            Orchestration + transaction boundary
├── dao/               JPA entities
│   ├── customer/      Customer + job cards + orders + history + membership
│   ├── employee/      Employee + qualifications + roster + activities
│   ├── services/      Services + groups + service types
│   ├── role/          Role / RoleName
│   └── repository/    JpaRepository<T, ID>
├── dto/               Records (auth) + classes (input/output shapes)
├── security/          JWT filter, token provider, user-details services
├── client/            Redis config + helper (refresh tokens only)
├── config/            SecurityFilterChain, WebMvc CORS, Auditing
├── aspect/            Request logging
├── interceptor/       RestClient logging
├── service/           Geonames service
├── exception/         Custom exceptions + GlobalExceptionHandler
├── utils/             AES-GCM, RSA-OAEP, name parsers, reference-data loaders
└── swagger/           OpenAPI config
```

## Quick start — Docker Compose

The fastest path. Builds the app jar in-container; brings up Postgres + Redis + Redis Commander
(key browser); fails fast if secrets aren't supplied.

```bash
# 1. Generate fresh dev secrets into ./.env  (openssl rand for HMAC + AES, openssl genpkey for RSA)
./scripts/generate-env.sh

# 2. Build + start everything
docker compose up --build

# Swagger UI:      http://localhost:8080/swagger-ui.html
# OpenAPI:         http://localhost:8080/v3/api-docs
# Redis Commander: http://localhost:8081  (bound to 127.0.0.1 only)
```

Other useful commands:

```bash
docker compose logs -f groomify    # tail app logs
docker compose down                # stop, keep data volumes
docker compose down -v             # stop + wipe DB/Redis
docker compose up -d --build       # rebuild and run detached
```

Seed five demo customers via the public API:

```bash
./scripts/seed.sh
# alice.demo / bob.demo / carol.demo / dave.demo / eve.demo  (all password Demo@1234)
```

The seed script polls `/v3/api-docs` until the app is ready, posts each customer in
`scripts/seed/customers.json` to `/api/auth/customer/signup`, then signs in as the first user and
fetches their profile to verify the seed actually landed. Idempotent — existing rows return 409 and
are reported as "already exists".

## Running locally without Docker

Prereqs: JDK 17, Maven 3.8+, PostgreSQL 13+, Redis 5+. The app reads every secret from environment
variables; see [.env.example](.env.example) for the full list.

```bash
createdb -U postgres groomify
psql -U postgres -c "CREATE USER groomify WITH PASSWORD 'changeme';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE groomify TO groomify;"

./scripts/generate-env.sh         # produces ./.env
set -a; source .env; set +a       # export the secrets into your shell
mvn clean package -DskipTests
java -jar target/groomify-0.0.1-SNAPSHOT.jar
```

Hibernate creates the schema on first boot (`ddl-auto=update`).

## Tests

```bash
mvn test    # 14 tests, ~8s, no Docker / Postgres / Redis daemon required
```

`AuthFlowEndToEndTest` boots the full Spring context against an in-process [embedded Redis](https://github.com/codemonstur/embedded-redis)
and an H2 datasource, then drives 13 end-to-end auth scenarios via `TestRestTemplate`:

- signup → signin → access + refresh tokens issued
- access token unlocks a protected endpoint (full round-trip)
- wrong / unknown / no-bearer / bare-token credentials → 401
- refresh-token flow + invalid refresh → 401
- username & email collision on signup → 409
- weak password rejected with structured 400
- public availability endpoint reachable without auth
- refresh token reusable across multiple refreshes

The harness lives in `src/test/java/com/uditgoel/groomify/support/EmbeddedRedisInitializer.java`.

## Auth flow

Two token types, intentionally asymmetric.

**Sign in** — `POST /api/auth/{customer,employee}/signin`
- Credentials verified via `BCryptPasswordEncoder`.
- `JwtTokenProvider` issues:
  - **Access JWT** — HS512-signed, 15 min default TTL. Subject payload (id / username / email /
    user type) is **AES-GCM** encrypted with a per-call random IV before being placed in `sub`,
    so the token is opaque and tamper-evident on the wire.
  - **Refresh token** — random string (33–45 ASCII range, 40 chars), **RSA-OAEP-SHA256**
    encrypted, stored in Redis under `TokenType/[refresh]/Token/[<ciphertext>]` with the subject
    payload as JSON value. TTL: 60 days.

**Authenticated request** — every endpoint
- `JwtAuthenticationFilter` requires `Authorization: Bearer <jwt>` exactly (a bare token without
  the prefix is rejected).
- Pure HS512 signature + expiry check — no Redis round-trip on the hot path.
- Trade-off: an access token can't be revoked instantly server-side. With a 15 min TTL the window
  is bounded; that's the canonical stateless-JWT pattern.

**Refresh** — `POST /api/auth/{customer,employee}/refreshToken`
- The only path that touches Redis. `RedisHelper.getRefreshTokenDetails(...)` looks up the
  subject; missing/invalid → 401. Found → `tokenProvider.createAccessToken(...)` issues a new
  access JWT.
- Revoking a session = deleting the Redis key.

Startup validation: the app refuses to boot unless `app.jwtSecret` is ≥ 64 bytes (HS512 minimum)
and `app.aes.key` decodes to 16/24/32 bytes — see `JwtTokenProvider` and `AppUtils`.

See [`security/JwtTokenProvider.java`](src/main/java/com/uditgoel/groomify/security/JwtTokenProvider.java),
[`security/JwtAuthenticationFilter.java`](src/main/java/com/uditgoel/groomify/security/JwtAuthenticationFilter.java),
[`utils/AppUtils.java`](src/main/java/com/uditgoel/groomify/utils/AppUtils.java),
[`client/RedisHelper.java`](src/main/java/com/uditgoel/groomify/client/RedisHelper.java).

## Password storage

- `Password` entity holds the **current** BCrypt hash + `pwdCreationDate` / `pwdExpirationDate`.
- `PasswordHistory` is a sibling `@OneToMany` from `Password` holding the last four hashes,
  newest first.
- On change: validate strength regex → reject if `newPassword` matches any of the last 5 hashes →
  current hash moves into history → new hash becomes current → history trimmed to `HISTORY_LIMIT`.

## API surface

| Path prefix | Purpose |
|---|---|
| `/api/auth/customer`, `/api/auth/employee` | `signin`, `signup`, `refreshToken` |
| `/api/customer` | Profile, uploads, password change, availability checks, name search |
| `/api/employee` | Employee CRUD, password reset, activities, attendance, vacation |
| `/api/chair` | Salon-chair allocation by floor + un-allocation / expiry |
| `/api/customerorder` | Appointment enquiries, orders, service updates, cancellation |
| `/api/customeremployee` | Cross-entity views (birthdays, calendar, enquiries) |
| `/api/validate` | Postal-code validation via geonames.org |

Errors come back as JSON via [`GlobalExceptionHandler`](src/main/java/com/uditgoel/groomify/exception/GlobalExceptionHandler.java):
`IllegalArgumentException` → 400, `IllegalStateException` → 503, `DataIntegrityViolationException`
→ 409, custom domain exceptions carry their own `@ResponseStatus`.

## Security

- **All secrets are env-var driven.** [`application.properties`](src/main/resources/application.properties)
  uses `${ENV_VAR:}` substitution everywhere; empty defaults force an explicit setting and the app
  refuses to boot otherwise. `.env` is gitignored — copy `.env.example` or run
  `./scripts/generate-env.sh` to produce one.
- **AES-GCM with random IV** for the JWT subject payload (was AES-CBC with a hardcoded fixed-IV
  reused-as-key in the original — replaced).
- **RSA-OAEP-SHA256** for refresh-token wrapping (was PKCS#1 v1.5 in the original).
- **Custom JSON unauthorized response** — `JwtAuthenticationEntryPoint` writes the body directly
  rather than using `sendError` (which can trigger Tomcat error-page dispatch and spurious
  `WWW-Authenticate` headers).
- **CORS** is opt-in. `groomify.cors.allowed-origins` is a comma-separated list; empty means no
  CORS mappings registered.
- **Gitleaks** runs on every push, PR, and weekly via cron — see
  [`.github/workflows/secret-scan.yml`](.github/workflows/secret-scan.yml) and
  [`.gitleaks.toml`](.gitleaks.toml). The allowlist exempts the deterministic test-only fixtures
  in `src/test/resources/application-test.properties` and the empty-placeholder shapes in
  `.env.example`.
- A weak HMAC secret was leaked in a 2019 commit and missed by an earlier `git filter-repo` pass.
  It has since been scrubbed from every blob and commit message across every branch. The current
  `JwtTokenProvider` rejects HS512 keys shorter than 64 bytes at startup, so the leak cannot forge
  JWTs against the modernized code path. If a pre-modernization deployment is still running with
  the leaked secret, rotate `APP_JWT_SECRET` there.

## What changed since the 2019 baseline

| | Before | After |
|---|---|---|
| Java | 8 | 17 |
| Spring Boot | 2.1.8 | 3.2.5 |
| `javax.*` → `jakarta.*` | – | every entity, filter, validation |
| Spring Security | `WebSecurityConfigurerAdapter` + `antMatchers` | `SecurityFilterChain` bean + `requestMatchers` |
| jjwt | 0.9 | 0.12 (`Jwts.parser().verifyWith(...)`, `Keys.hmacShaKeyFor`) |
| API docs | springfox 2.9 | springdoc-openapi 2.3 |
| Database | MySQL | PostgreSQL 16 (H2 for tests) |
| Repositories | `PagingAndSortingRepository<T, ID>` | `JpaRepository<T, ID>` |
| Packaging | WAR (provided-tomcat) | JAR (embedded Tomcat) |
| Access-token validation | round-trips Redis | pure signature + expiry, no Redis on hot path |
| Password history | 5 fixed columns `PASSWORD_1..5` | `Password` + `PasswordHistory` table |
| Subject payload encryption | AES-CBC with hardcoded fixed-IV reused as key | AES-GCM with per-call random IV |
| Refresh-token wrapping | RSA + PKCS#1 v1.5 padding | RSA-OAEP-SHA256 padding |
| DTOs | hand-written getters/setters | Java records (`LoginRequest`, `JwtJsonSubjectKey`, `JwtAuthenticationResponse`) |
| Bean injection | `@Resource` field injection (26 files) | constructor injection, final fields |
| `UserDetailsService` dispatch | reflective `appCtx.getBean(name)` lookup | `Map<UserType, UserDetailsService>` injected at construction |
| Date types in JWT | `Calendar`/`Date` | `Instant` (ISO-8601 on the wire) |
| Tests | 1 stub (context loads) | 14 tests, 13 end-to-end, no Docker required |
| Docker | – | multi-stage Dockerfile + compose with Postgres / Redis / Redis Commander |
| Secret config | hardcoded in `application.properties` | env-var substitution, generator script, gitleaks CI |
| Endpoint bugs | several JPQL bugs (`ORDER BY DOB`, `=` with LIKE wildcards, wrong entity in `existsByEmail`); `@MapsId` referencing column names; missing `@Service` on ServiceFacadeImpl; `regId` silently dropped on signup; password-history check used `currentPass` (always rejecting changes); employee password used `UserType.CUSTOMER`; raw password leaked into error messages | all fixed |
| Transactions | none | class-level `@Transactional(readOnly=true)` on every facade, method-level `@Transactional` on writes — required after `open-in-view=false` |
| Error responses | inconsistent (Tomcat default error page, spurious 500s) | `GlobalExceptionHandler` translates common exceptions to clean JSON 4xx/5xx |
| Bearer parsing | accepted both `Bearer <jwt>` and bare token | strict — only `Bearer ` prefix accepted |

## Caveats & known follow-ups

- **`LoadAddressMetaTable`** is opt-in via `groomify.bootstrap.address-meta=true`; it makes a
  blocking HTTP fan-out to geonames.org that can take several minutes. Not run by default — set
  the flag only on first-time bootstrap.
- **Triple `*UserDetailsService`** (`Custom{Customer,Employee,User}DetailsService`) and twin
  `*AuthController` are ~90% duplicated. A generic strategy would cut ~600 LOC. Left in place
  because it works; refactor is non-trivial and changes API path conventions.
- **Single-instance rate limiter.** [`SigninRateLimitFilter`](src/main/java/com/uditgoel/groomify/security/SigninRateLimitFilter.java)
  is per-IP, fixed-window-per-minute, in-memory. Fine for a single node; multi-instance
  deployments need a shared store (Redis bucket) or an upstream WAF — otherwise a caller gets
  N×instances attempts before the first one trips.
- **No standalone observability** — no Actuator/Prometheus, no structured tracing. Fine for a
  reference; add `spring-boot-starter-actuator` + Micrometer for production.

### Resolved during the modernization round

- Endpoint-level **IDOR ownership checks** via `@PreAuthorize("authentication.name == #username")`
  on every customer/employee/customer-order endpoint that takes a user identifier in the path.
  Verified by `accessToken_cannotFetch_otherUsersProfile_returns403` in the e2e suite.
- **Reference-data loaders** are now `@Transactional` — `count == 0` check + `saveAll` are atomic
  per loader.
- **Signin rate limit** (per-IP, `groomify.signin.max-attempts-per-minute`, default 10) sits in
  front of the auth filter so brute-force probes never reach BCrypt.
- **`@PostConstruct` validation** at startup for `app.jwtSecret` (≥ 64 bytes), `app.aes.key`
  (16/24/32 bytes Base64) — app refuses to boot otherwise. Replaces the old "errors at first
  auth" caveat.

## License

MIT — see [LICENSE](LICENSE).
