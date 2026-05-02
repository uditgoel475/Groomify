# P0 Production-Readiness Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Land the four production-readiness blockers from the design spec on a single branch (`chore/p0-prod-readiness`): Flyway migrations replacing `ddl-auto=update`, Spring Boot Actuator + Prometheus + JSON-structured logging, refresh-token rotation with reuse detection, and Bean Validation on every input DTO.

**Architecture:** Each item lands as its own task block with TDD discipline (failing test first, minimal implementation, pass, commit). The four items are independent enough that any one can be reverted without unwinding the others. Existing 15-test e2e suite must stay green throughout; +4 new tests by the end.

**Tech Stack:** Spring Boot 3.2.5, Java 17, Postgres 16 (prod) / H2 (tests), Flyway 10.x, Micrometer Prometheus, Logstash Logback Encoder, Hibernate Validator 8 (bundled with Spring Boot 3.2 via `spring-boot-starter-validation`), JUnit 5 + AssertJ + TestRestTemplate.

**Plan covers:** Spec items #1–#4 (P0 only). Items #5–#16 will get their own plans after this PR merges.

---

## Phase 0 — Prerequisites

### Task 0.1: Create the feature branch

**Files:** none

- [ ] **Step 1: Confirm clean working tree on master**

Run: `cd /Users/Udit_Goel/Desktop/Practise/Jobs/Groomify && git status -sb`
Expected: `## master...origin/master` and no working-tree changes.

- [ ] **Step 2: Branch off**

Run: `git checkout -b chore/p0-prod-readiness`
Expected: `Switched to a new branch 'chore/p0-prod-readiness'`

- [ ] **Step 3: Confirm baseline test suite is green**

Run: `mvn test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0` and `BUILD SUCCESS`.

If any test fails — STOP. Don't start P0 work on a red baseline.

---

## Item #1 — Flyway migrations replacing `ddl-auto=update`

### Task 1.1: Add Flyway dependencies

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: Add Flyway core + postgresql plugin to `<dependencies>`**

Insert after the `org.postgresql:postgresql` dependency (around line 86):

```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

(Spring Boot's BOM manages the version, so no `<version>` needed.)

- [ ] **Step 2: Verify the dependency tree**

Run: `mvn -q dependency:tree | grep flyway`
Expected: Two lines, `flyway-core` and `flyway-database-postgresql` resolved at Spring-Boot-managed version (10.x).

- [ ] **Step 3: Verify the project still compiles**

Run: `mvn -q compile`
Expected: Exits 0 with no output.

### Task 1.2: Generate the Flyway baseline SQL from the running schema

**Files:**
- Create: `src/main/resources/db/migration/V1__baseline.sql`

- [ ] **Step 1: Bring up a temporary Postgres for schema dump**

Run:
```bash
docker run --rm --name groomify-baseline-pg -d \
  -e POSTGRES_DB=groomify_baseline \
  -e POSTGRES_USER=groomify \
  -e POSTGRES_PASSWORD=temp \
  -p 5433:5432 \
  postgres:16-alpine
sleep 5
```

Expected: Container running; `docker ps | grep groomify-baseline-pg` shows it.

- [ ] **Step 2: Boot the app once against this DB so Hibernate creates the schema**

Run (in a separate terminal or backgrounded):
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/groomify_baseline \
SPRING_DATASOURCE_USERNAME=groomify \
SPRING_DATASOURCE_PASSWORD=temp \
APP_JWT_SECRET="$(openssl rand -base64 64 | tr -d '\n')" \
APP_AES_KEY="$(openssl rand -base64 32)" \
RSA_PRIVATE_KEY="$(openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 2>/dev/null | openssl pkcs8 -topk8 -inform PEM -outform DER -nocrypt | base64 | tr -d '\n')" \
RSA_PUBLIC_KEY="placeholder" \
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=baseline-gen" &
APP_PID=$!
sleep 30
kill $APP_PID
```

Expected: App starts, "Started GroomifyApplication" appears, Hibernate creates ~30 tables.

- [ ] **Step 3: Dump schema-only**

Run:
```bash
docker exec groomify-baseline-pg \
  pg_dump --schema-only --no-owner --no-privileges -U groomify groomify_baseline \
  > /tmp/groomify-baseline-raw.sql
```

Expected: File ~10–20 KB containing all `CREATE TABLE` / `ALTER TABLE` / `CREATE SEQUENCE` statements.

- [ ] **Step 4: Hand-curate into `V1__baseline.sql`**

Copy `/tmp/groomify-baseline-raw.sql` to `src/main/resources/db/migration/V1__baseline.sql`. Then strip:
- The `SET` statements at the top (Flyway sets these itself)
- Any `flyway_schema_history` table reference (Flyway creates this)
- `CREATE EXTENSION` lines if present (we don't use any extensions)
- Comments not relevant to schema

Keep:
- `CREATE TABLE` statements for every entity
- `CREATE SEQUENCE` statements
- `ALTER TABLE ... ADD CONSTRAINT` statements (PKs, FKs, uniques, checks)
- `CREATE INDEX` statements

Add a header comment:
```sql
-- Baseline schema for Groomify, captured 2026-05-02 from Hibernate-generated
-- schema. This is the starting point for all future Flyway migrations.
```

- [ ] **Step 5: Tear down the temp Postgres**

Run: `docker rm -f groomify-baseline-pg`
Expected: Container removed.

- [ ] **Step 6: Sanity-check the baseline file is non-trivial**

Run: `wc -l src/main/resources/db/migration/V1__baseline.sql`
Expected: ≥ 200 lines (actual schema has many tables).

### Task 1.3: Configure Flyway in `application.properties`

**Files:**
- Modify: `src/main/resources/application.properties`

- [ ] **Step 1: Add Flyway config + flip `ddl-auto` from `update` to `validate`**

In the `# --- JPA / Hibernate ---` section, replace:
```properties
spring.jpa.hibernate.ddl-auto=update
```
with:
```properties
spring.jpa.hibernate.ddl-auto=validate
```

After the JPA block, add:
```properties
# --- Flyway ---
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=true
```

- [ ] **Step 2: Disable Flyway in the test profile (H2 doesn't need it)**

Modify `src/test/resources/application-test.properties`. Add:
```properties
# H2 doesn't need Flyway; Hibernate's create-drop is enough for tests.
spring.flyway.enabled=false
```

- [ ] **Step 3: Verify the test suite still passes (without Flyway)**

Run: `mvn test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: 15/15 still green; H2 path unaffected.

### Task 1.4: Add a Flyway integration test

**Files:**
- Create: `src/test/java/com/uditgoel/groomify/FlywayMigrationTest.java`

- [ ] **Step 1: Write the test**

Create the file with the following content:

```java
package com.uditgoel.groomify;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Boots the app against a real Postgres via Testcontainers and asserts Flyway ran
 * (its bookkeeping table exists and has at least one successful row).
 *
 * <p>Skipped automatically when Docker is unavailable on the host.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers(disabledWithoutDocker = true)
class FlywayMigrationTest {

    @Container
    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("groomify_test")
            .withUsername("groomify")
            .withPassword("test");

    @DynamicPropertySource
    static void registerProps(DynamicPropertyRegistry registry) throws Exception {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");

        // Required secrets for the app to boot.
        registry.add("app.jwtSecret", () -> "test-secret-test-secret-test-secret-test-secret-test-secret-1234");
        registry.add("app.aes.key", () -> "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8=");
        KeyPair kp = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        registry.add("rsa.public.key", () -> Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
        registry.add("rsa.private.key", () -> Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));

        // Redis is required by RedisHelper at startup; point at a non-existent host
        // and rely on RedisHelper.isRedisWorking() returning false (it doesn't fail fast).
        registry.add("datasource.redis.master.host", () -> "127.0.0.1");
        registry.add("datasource.redis.master.port", () -> "16379");
        registry.add("datasource.redis.master.pass", () -> "");
        registry.add("datasource.redis.slave.host", () -> "127.0.0.1");
        registry.add("datasource.redis.slave.port", () -> "16380");
        registry.add("datasource.redis.slave.pass", () -> "");
    }

    @Autowired
    private DataSource dataSource;

    @Test
    void flyway_baseline_runs_successfully() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        Integer historyRows = jdbc.queryForObject(
                "SELECT count(*) FROM flyway_schema_history WHERE success = true",
                Integer.class);
        assertThat(historyRows).isNotNull().isGreaterThanOrEqualTo(1);

        Integer customerTableExists = jdbc.queryForObject(
                "SELECT count(*) FROM information_schema.tables WHERE table_name = 'customer'",
                Integer.class);
        assertThat(customerTableExists).isEqualTo(1);
    }
}
```

- [ ] **Step 2: Run the test**

Run: `mvn -Dtest=FlywayMigrationTest test 2>&1 | grep -E "Tests run:|BUILD" | head -3`

Expected (Docker available): `Tests run: 1, Failures: 0, Errors: 0, Skipped: 0` and `BUILD SUCCESS`.
Expected (Docker not available): `Tests run: 0` (skipped via `disabledWithoutDocker = true`) — still `BUILD SUCCESS`.

If failures: read the error. Most likely cause is the `V1__baseline.sql` mismatching what Hibernate expects. Adjust the SQL and re-run.

- [ ] **Step 3: Run the FULL test suite to confirm no regression**

Run: `mvn test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 16` (one new test) and `BUILD SUCCESS`.

### Task 1.5: Commit Item #1

- [ ] **Step 1: Commit**

```bash
git add pom.xml src/main/resources/application.properties \
        src/main/resources/db src/test/resources/application-test.properties \
        src/test/java/com/uditgoel/groomify/FlywayMigrationTest.java
git -c commit.gpgsign=false commit -m "$(cat <<'EOF'
P0 #1: Flyway migrations replacing ddl-auto=update

- spring.jpa.hibernate.ddl-auto: update -> validate
- Add flyway-core + flyway-database-postgresql
- V1__baseline.sql captured from Hibernate-generated schema
- baseline-on-migrate=true so existing dev volumes don't need wiping
- H2 test profile keeps create-drop and disables Flyway
- New FlywayMigrationTest boots against Testcontainers Postgres and
  asserts flyway_schema_history is populated and the customer table
  exists. Skipped automatically when Docker isn't available.

Tests: 16/16 green.
EOF
)"
```

Expected: One commit on `chore/p0-prod-readiness`.

---

## Item #2 — Spring Boot Actuator + Prometheus + JSON logging

### Task 2.1: Add Actuator + Micrometer + Logstash dependencies

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: Add the three deps**

Insert after the existing Spring Boot starters (around line 78, before the `org.postgresql` dep):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

- [ ] **Step 2: Verify dep tree**

Run: `mvn -q dependency:tree | grep -E "actuator|micrometer-registry-prometheus|logstash-logback-encoder"`
Expected: All three resolve.

### Task 2.2: Configure Actuator endpoints

**Files:**
- Modify: `src/main/resources/application.properties`

- [ ] **Step 1: Append actuator config to the file**

Add at the end:
```properties
# --- Actuator ---
# Expose only health/info/prometheus over HTTP. Everything else stays internal.
management.endpoints.web.exposure.include=health,info,prometheus
management.endpoint.health.probes.enabled=true
management.endpoint.health.show-details=when-authorized
management.metrics.tags.application=groomify
```

### Task 2.3: Permit Actuator endpoints in SecurityConfig

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/config/SecurityConfig.java`

- [ ] **Step 1: Add `/actuator/**` paths to the permitAll list**

Find the block:
```java
.requestMatchers(
    "/error",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html"
).permitAll()
```

Replace with:
```java
.requestMatchers(
    "/error",
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/actuator/health",
    "/actuator/health/**",
    "/actuator/info",
    "/actuator/prometheus"
).permitAll()
```

### Task 2.4: Add structured JSON logging via `logback-spring.xml`

**Files:**
- Create: `src/main/resources/logback-spring.xml`

- [ ] **Step 1: Create the file**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/defaults.xml"/>

    <!-- Plain console output for dev / default profile. -->
    <springProfile name="default | dev | test">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>

    <!-- JSON-structured logs for prod (Datadog/ELK-friendly). -->
    <springProfile name="prod">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder class="net.logstash.logback.encoder.LogstashEncoder">
                <customFields>{"application":"groomify"}</customFields>
            </encoder>
        </appender>
        <root level="INFO">
            <appender-ref ref="CONSOLE"/>
        </root>
    </springProfile>
</configuration>
```

- [ ] **Step 2: Remove the now-redundant pattern config from `application.properties`**

Find and DELETE these lines:
```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} | %msg%n
logging.pattern.file=[%thread] %-5level %logger{36} - %d{yyyy-MM-dd HH:mm:ss} | %msg%n
```

Keep the `logging.level.*` and `logging.file.name` lines.

### Task 2.5: Add the Actuator e2e test

**Files:**
- Create: `src/test/java/com/uditgoel/groomify/ActuatorHealthTest.java`

- [ ] **Step 1: Write the failing test**

```java
package com.uditgoel.groomify;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.uditgoel.groomify.support.EmbeddedRedisInitializer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ContextConfiguration(initializers = EmbeddedRedisInitializer.class)
class ActuatorHealthTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    @Test
    void actuatorHealth_returnsUp_andIsPublic() {
        ResponseEntity<String> resp = rest.getForEntity(
                "http://localhost:" + port + "/actuator/health", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("\"status\":\"UP\"");
    }

    @Test
    void actuatorPrometheus_isReachable() {
        ResponseEntity<String> resp = rest.getForEntity(
                "http://localhost:" + port + "/actuator/prometheus", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("jvm_memory_used_bytes");
    }
}
```

- [ ] **Step 2: Run the new tests**

Run: `mvn -Dtest=ActuatorHealthTest test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 2, Failures: 0, Errors: 0` and `BUILD SUCCESS`.

If 401/403: SecurityConfig isn't permitting the path. Re-check Task 2.3.
If 404: Actuator endpoints not registered. Verify the dep was actually added in Task 2.1.

- [ ] **Step 3: Run full test suite**

Run: `mvn test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 18` (was 16, +2 for the two new tests), `BUILD SUCCESS`.

### Task 2.6: Commit Item #2

- [ ] **Step 1: Commit**

```bash
git add pom.xml src/main/resources/application.properties \
        src/main/resources/logback-spring.xml \
        src/main/java/com/uditgoel/groomify/config/SecurityConfig.java \
        src/test/java/com/uditgoel/groomify/ActuatorHealthTest.java
git -c commit.gpgsign=false commit -m "$(cat <<'EOF'
P0 #2: Actuator + Prometheus + JSON-structured logging

- spring-boot-starter-actuator + micrometer-registry-prometheus
- net.logstash.logback:logstash-logback-encoder
- Actuator exposes only health (with probes), info, prometheus over HTTP
- SecurityConfig permits /actuator/health, /actuator/health/**,
  /actuator/info, /actuator/prometheus
- New logback-spring.xml: plain console for default/dev/test profiles,
  Logstash JSON encoder for prod profile (ELK / Datadog friendly)
- New ActuatorHealthTest covers /actuator/health (UP) and
  /actuator/prometheus (jvm_memory_used_bytes present)

Tests: 18/18 green.
EOF
)"
```

---

## Item #3 — Refresh-token rotation with reuse detection

### Task 3.1: Define the new refresh-token Redis value shape

**Files:**
- Create: `src/main/java/com/uditgoel/groomify/security/RefreshTokenRecord.java`

- [ ] **Step 1: Create the record**

```java
package com.uditgoel.groomify.security;

import java.time.Instant;

import com.uditgoel.groomify.dto.JwtJsonSubjectKey;

/**
 * Redis-stored value for a refresh token. Per-token state for the
 * rotation-with-reuse-detection pattern:
 *
 * <ul>
 *   <li>{@code subject} — what the token represents (user identity).</li>
 *   <li>{@code familyId} — every refresh-token chain that started from one signin
 *       shares a family. On reuse-after-rotation we wipe the entire family.</li>
 *   <li>{@code rotatedAt} — null while the token is still valid; set to now() when
 *       rotation happens. A second presentation of a rotated token is a theft signal.</li>
 * </ul>
 */
public record RefreshTokenRecord(JwtJsonSubjectKey subject, String familyId, Instant rotatedAt) {
}
```

### Task 3.2: Extend `RedisHelper` with family-aware operations

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/client/RedisHelper.java`

- [ ] **Step 1: Read the existing file**

Run: `cat src/main/java/com/uditgoel/groomify/client/RedisHelper.java`
Note: existing methods are `createRedisJWTRefreshToken(String token, JwtJsonSubjectKey subject)` and `getRefreshTokenDetails(String token)`. We're going to keep those (for backward compat with old tokens) and add new family-aware overloads.

- [ ] **Step 2: Add the new value format key + family set helpers**

Append these methods to the class (before the closing `}`):

```java
// --- Refresh-token rotation (familyId + rotatedAt) ---

private String familyKey(String familyId) {
    return "refresh:family:" + familyId;
}

public void storeRefreshTokenWithFamily(String token, RefreshTokenRecord record)
        throws JsonProcessingException {
    String key = String.format(jwtRefreshTokenKey, token);
    redisClient.setValue(key, objectMapper.writeValueAsString(record),
            refreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
    redisClient.sadd(familyKey(record.familyId()), token);
}

public RefreshTokenRecord getRefreshTokenRecord(String token) throws IOException {
    String key = String.format(jwtRefreshTokenKey, token);
    String value = redisClient.getValue(key);
    if (StringUtils.isEmpty(value)) {
        return null;
    }
    return objectMapper.readValue(value, RefreshTokenRecord.class);
}

public void markRotated(String token, RefreshTokenRecord record) throws JsonProcessingException {
    String key = String.format(jwtRefreshTokenKey, token);
    RefreshTokenRecord rotated = new RefreshTokenRecord(
            record.subject(), record.familyId(), java.time.Instant.now());
    redisClient.setValue(key, objectMapper.writeValueAsString(rotated),
            refreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
}

public void wipeFamily(String familyId) {
    String setKey = familyKey(familyId);
    java.util.Set<String> tokens = redisClient.smembers(setKey);
    if (tokens != null) {
        for (String t : tokens) {
            redisClient.delete(String.format(jwtRefreshTokenKey, t));
        }
    }
    redisClient.delete(setKey);
}
```

Add the import for `RefreshTokenRecord`:
```java
import com.uditgoel.groomify.security.RefreshTokenRecord;
```

- [ ] **Step 3: Add the missing primitives to `RedisClient` (sadd, smembers)**

Read: `src/main/java/com/uditgoel/groomify/client/RedisClient.java`

If `sadd(String, String)` and `smembers(String)` don't exist, add them. They wrap the Lettuce/Jedis primitive (the existing class uses a Spring Data Redis template — examine it to know which API to use). Likely:

```java
public void sadd(String key, String value) {
    redisTemplate.opsForSet().add(key, value);
}

public java.util.Set<String> smembers(String key) {
    return redisTemplate.opsForSet().members(key);
}
```

- [ ] **Step 4: Compile**

Run: `mvn -q compile`
Expected: Exits 0.

### Task 3.3: Update `JwtTokenProvider` to issue rotation-aware refresh tokens

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/security/JwtTokenProvider.java`

- [ ] **Step 1: Extend `generateToken` to use the new family-aware store**

Find:
```java
public JwtAuthenticationResponse generateToken(Authentication authentication, UserType userType)
        throws JsonProcessingException {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    String refreshToken = createRefreshToken(userPrincipal.getUsername(), userType.toString());

    JwtJsonSubjectKey subjectKey = new JwtJsonSubjectKey(userPrincipal.getId(), userPrincipal.getUsername(),
            userPrincipal.getEmail(), userType);
    if (redisHelper.isRedisWorking()) {
        redisHelper.createRedisJWTRefreshToken(refreshToken, subjectKey);
    }
    return issueAccessToken(subjectKey, refreshToken);
}
```

Replace with:
```java
public JwtAuthenticationResponse generateToken(Authentication authentication, UserType userType)
        throws JsonProcessingException {

    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    String refreshToken = createRefreshToken(userPrincipal.getUsername(), userType.toString());

    JwtJsonSubjectKey subjectKey = new JwtJsonSubjectKey(userPrincipal.getId(), userPrincipal.getUsername(),
            userPrincipal.getEmail(), userType);
    if (redisHelper.isRedisWorking()) {
        String familyId = java.util.UUID.randomUUID().toString();
        redisHelper.storeRefreshTokenWithFamily(refreshToken,
                new RefreshTokenRecord(subjectKey, familyId, null));
    }
    return issueAccessToken(subjectKey, refreshToken);
}
```

- [ ] **Step 2: Replace `getJwtJsonSubjectKeyFromRefreshToken` with rotation-aware logic**

Find the existing method:
```java
public Optional<JwtJsonSubjectKey> getJwtJsonSubjectKeyFromRefreshToken(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
        return Optional.empty();
    }
    if (!redisHelper.isRedisWorking()) {
        throw new IllegalStateException("Redis unavailable; cannot resolve refresh token");
    }
    try {
        return Optional.ofNullable(redisHelper.getRefreshTokenDetails(refreshToken));
    } catch (IOException e) {
        logger.error("Could not deserialize refresh token payload: {}", e.getMessage());
        return Optional.empty();
    }
}
```

Replace with:
```java
/**
 * Resolves a refresh token AND rotates it: marks the presented token as rotated,
 * issues a new refresh token, returns both the subject and the new token.
 *
 * <p>If the presented token has already been rotated (rotatedAt != null), this is
 * token reuse — almost certainly theft — and we wipe the entire family.
 *
 * @return a {@link RotationResult} on success; empty if the token is unknown,
 *         expired, or its family was already wiped due to a prior reuse.
 */
public Optional<RotationResult> rotateRefreshToken(String refreshToken) {
    if (refreshToken == null || refreshToken.isBlank()) {
        return Optional.empty();
    }
    if (!redisHelper.isRedisWorking()) {
        throw new IllegalStateException("Redis unavailable; cannot resolve refresh token");
    }
    try {
        RefreshTokenRecord record = redisHelper.getRefreshTokenRecord(refreshToken);
        if (record == null) {
            return Optional.empty();
        }
        if (record.rotatedAt() != null) {
            // Theft signal: a previously-rotated token is being presented again.
            logger.warn("Refresh-token reuse detected for family {} (user {}); wiping family",
                    record.familyId(), record.subject().username());
            redisHelper.wipeFamily(record.familyId());
            return Optional.empty();
        }
        // Mark old token rotated; mint and store a new one in the same family.
        redisHelper.markRotated(refreshToken, record);
        String newToken = createRefreshToken(record.subject().username(),
                record.subject().userType().toString());
        redisHelper.storeRefreshTokenWithFamily(newToken,
                new RefreshTokenRecord(record.subject(), record.familyId(), null));
        return Optional.of(new RotationResult(record.subject(), newToken));
    } catch (IOException | com.fasterxml.jackson.core.JsonProcessingException e) {
        logger.error("Could not process refresh token: {}", e.getMessage());
        return Optional.empty();
    }
}

/** Result of a successful refresh-token rotation. */
public record RotationResult(JwtJsonSubjectKey subject, String newRefreshToken) {
}
```

Remove the old `getJwtJsonSubjectKeyFromRefreshToken` once nothing references it (we'll update the callers next).

Add import:
```java
import com.uditgoel.groomify.security.RefreshTokenRecord;
```

### Task 3.4: Update auth controllers to use the new rotation API

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/controller/CustomerAuthController.java`
- Modify: `src/main/java/com/uditgoel/groomify/controller/EmployeeAuthController.java`

- [ ] **Step 1: Update `CustomerAuthController` refreshToken endpoint**

Find:
```java
@PostMapping("refreshToken")
public ResponseEntity<JwtAuthenticationResponse> regenerateAccessToken(
        @RequestBody Map<String, String> refreshTokenMap) throws JsonProcessingException {
    String refreshToken = refreshTokenMap.get("refreshToken");
    JwtJsonSubjectKey jwtJsonSubjectKey = tokenProvider.getJwtJsonSubjectKeyFromRefreshToken(refreshToken)
            .orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));
    return ResponseEntity.ok(tokenProvider.createAccessToken(jwtJsonSubjectKey, refreshToken));
}
```

Replace with:
```java
@PostMapping("refreshToken")
public ResponseEntity<JwtAuthenticationResponse> regenerateAccessToken(
        @RequestBody Map<String, String> refreshTokenMap) throws JsonProcessingException {
    String refreshToken = refreshTokenMap.get("refreshToken");
    JwtTokenProvider.RotationResult rotation = tokenProvider.rotateRefreshToken(refreshToken)
            .orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));
    return ResponseEntity.ok(tokenProvider.createAccessToken(rotation.subject(), rotation.newRefreshToken()));
}
```

- [ ] **Step 2: Same change in `EmployeeAuthController`**

Apply the identical replacement (s/customer/employee/ in URL, but the body is the same) to `EmployeeAuthController.java`.

- [ ] **Step 3: Compile**

Run: `mvn -q compile`
Expected: 0 errors. If callers of the old `getJwtJsonSubjectKeyFromRefreshToken` exist elsewhere, the compiler will surface them.

### Task 3.5: Add the rotation e2e test

**Files:**
- Modify: `src/test/java/com/uditgoel/groomify/AuthFlowEndToEndTest.java`

- [ ] **Step 1: Add a new test method at the end of the class**

Insert before the closing `}` and the `validCustomerSignup` helper:

```java
@Test
void refreshToken_rotates_oldOneRejected() {
    String username = "rot1-" + System.nanoTime();
    String password = "Test@1234";
    rest.postForEntity(url("/api/auth/customer/signup"),
            validCustomerSignup(username, username + "@example.com", password), String.class);
    String t1 = rest.postForEntity(url("/api/auth/customer/signin"),
            Map.of("username", username, "password", password), JwtAuthenticationResponse.class)
            .getBody().refreshToken();

    ResponseEntity<JwtAuthenticationResponse> firstRefresh = rest.postForEntity(
            url("/api/auth/customer/refreshToken"),
            Map.of("refreshToken", t1), JwtAuthenticationResponse.class);
    assertThat(firstRefresh.getStatusCode()).isEqualTo(HttpStatus.OK);
    String t2 = firstRefresh.getBody().refreshToken();
    assertThat(t2).isNotEqualTo(t1);

    ResponseEntity<String> reuseT1 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
            Map.of("refreshToken", t1), String.class);
    assertThat(reuseT1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
}

@Test
void refreshTokenReuse_wipesEntireFamily() {
    String username = "rot2-" + System.nanoTime();
    String password = "Test@1234";
    rest.postForEntity(url("/api/auth/customer/signup"),
            validCustomerSignup(username, username + "@example.com", password), String.class);
    String t1 = rest.postForEntity(url("/api/auth/customer/signin"),
            Map.of("username", username, "password", password), JwtAuthenticationResponse.class)
            .getBody().refreshToken();
    String t2 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
            Map.of("refreshToken", t1), JwtAuthenticationResponse.class)
            .getBody().refreshToken();

    // Reuse t1 — theft signal — should wipe the family (t1 + t2).
    ResponseEntity<String> reuseT1 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
            Map.of("refreshToken", t1), String.class);
    assertThat(reuseT1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    // t2 should also now be invalid — family was wiped.
    ResponseEntity<String> reuseT2 = rest.postForEntity(url("/api/auth/customer/refreshToken"),
            Map.of("refreshToken", t2), String.class);
    assertThat(reuseT2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
}
```

- [ ] **Step 2: Run the new tests**

Run: `mvn -Dtest=AuthFlowEndToEndTest#refreshToken_rotates_oldOneRejected+refreshTokenReuse_wipesEntireFamily test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 2, Failures: 0, Errors: 0` and `BUILD SUCCESS`.

If failures: re-read the JwtTokenProvider changes, especially that `markRotated` actually persists the `rotatedAt` field, and `getRefreshTokenRecord` reads it back.

- [ ] **Step 3: Run full suite**

Run: `mvn test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 20` (was 18, +2), `BUILD SUCCESS`.

### Task 3.6: Commit Item #3

- [ ] **Step 1: Commit**

```bash
git add src/main/java/com/uditgoel/groomify/security/RefreshTokenRecord.java \
        src/main/java/com/uditgoel/groomify/security/JwtTokenProvider.java \
        src/main/java/com/uditgoel/groomify/client/RedisHelper.java \
        src/main/java/com/uditgoel/groomify/client/RedisClient.java \
        src/main/java/com/uditgoel/groomify/controller/CustomerAuthController.java \
        src/main/java/com/uditgoel/groomify/controller/EmployeeAuthController.java \
        src/test/java/com/uditgoel/groomify/AuthFlowEndToEndTest.java
git -c commit.gpgsign=false commit -m "$(cat <<'EOF'
P0 #3: Refresh-token rotation with reuse detection

Standard rotation-with-theft-detection pattern:

- New RefreshTokenRecord (subject, familyId, rotatedAt) replaces the old
  bare JwtJsonSubjectKey value in Redis.
- Each /refreshToken call: lookup, mark rotated, issue new token in same
  family, return new pair. Reuse of an already-rotated token is treated as
  theft — entire family wiped.
- familyId tracked via a per-family Redis Set (refresh:family:<uuid>) for
  atomic wipe.
- New: rotateRefreshToken() returns Optional<RotationResult>; the old
  getJwtJsonSubjectKeyFromRefreshToken removed.
- Both auth controllers (Customer + Employee) updated.

Two new e2e tests cover happy-path rotation and theft-detection wipe.

Tests: 20/20 green.
EOF
)"
```

---

## Item #4 — Bean Validation on input DTOs

### Task 4.1: Annotate `CustomerDTO` (and parent `CustomerOutDTO`) for validation

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/dto/customer/CustomerDTO.java`
- Modify: `src/main/java/com/uditgoel/groomify/dto/customer/CustomerOutDTO.java`

- [ ] **Step 1: Add field annotations**

In `CustomerOutDTO.java`, add imports:
```java
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
```

Annotate the fields:
```java
@NotBlank @Size(max = 64) private String username;
@NotBlank @Size(max = 128) private String name;
@NotNull @Past private Date dob;
private long contact;
private long alternateContact;
@NotBlank private String gender;
private AddressInput billingAddress;
private AddressInput shippingAddress;
@NotBlank private String regId;
@NotBlank @Email @Size(max = 40) private String email;
```

In `CustomerDTO.java`, add imports:
```java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
```

Annotate the additional fields:
```java
private MultipartFile pictureFile;
@NotBlank
@Size(min = 8, max = 16, message = "password must be 8-16 characters")
@Pattern(regexp = "(?=.*[A-Z])(?=.*[!@#$&*_])(?=.*\\d)(?=.*[a-z]).{8,16}",
         message = "password must include uppercase, lowercase, digit and special character")
private String password;

private boolean isSameShipping;
@NotBlank private String govtIdType;
@NotBlank private String govtId;
private MultipartFile govtIdPic;
```

### Task 4.2: Annotate `EmployeeInput` (and parent `EmployeeDTO`)

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/dto/employee/EmployeeInput.java`
- Modify: `src/main/java/com/uditgoel/groomify/dto/employee/EmployeeDTO.java`

- [ ] **Step 1: Add the same kind of annotations**

`EmployeeDTO`:
```java
@NotBlank @Size(max = 64) private String username;
@NotBlank @Size(max = 128) private String name;
@NotNull @Past private Date dob;
@NotBlank private String gender;
@NotBlank @Email @Size(max = 40) private String email;
```

`EmployeeInput`:
```java
@NotBlank
@Pattern(regexp = "(?=.*[A-Z])(?=.*[!@#$&*_])(?=.*\\d)(?=.*[a-z]).{8,16}")
private String password;

@NotBlank private String govtIdType;
@NotBlank private String govtId;
@DecimalMin("0.0") private double salary;
@NotNull @Past private Date joiningDate;
@NotNull private RoleName roleName;
```

(Imports as before plus `jakarta.validation.constraints.DecimalMin`.)

### Task 4.3: Add `@Valid` to controllers' `@RequestBody` parameters

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/controller/CustomerAuthController.java`
- Modify: `src/main/java/com/uditgoel/groomify/controller/EmployeeAuthController.java`
- Modify: `src/main/java/com/uditgoel/groomify/controller/CustomerOrderController.java`
- Modify: `src/main/java/com/uditgoel/groomify/controller/CustomerController.java`
- Modify: `src/main/java/com/uditgoel/groomify/controller/EmployeeController.java`

- [ ] **Step 1: For each controller, prefix every `@RequestBody` parameter with `@Valid`**

Example transformation:

Before:
```java
public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerInput) {
```

After:
```java
public ResponseEntity<String> createCustomer(@Valid @RequestBody CustomerDTO customerInput) {
```

Add the import where missing:
```java
import jakarta.validation.Valid;
```

Apply to every `@RequestBody` in all five controllers.

### Task 4.4: Map `MethodArgumentNotValidException` in `GlobalExceptionHandler`

**Files:**
- Modify: `src/main/java/com/uditgoel/groomify/exception/GlobalExceptionHandler.java`

- [ ] **Step 1: Add the new handler**

Add this method to the class:

```java
@ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
public ResponseEntity<Map<String, Object>> handleValidation(
        org.springframework.web.bind.MethodArgumentNotValidException ex,
        HttpServletRequest request) {
    java.util.List<Map<String, Object>> errors = new java.util.ArrayList<>();
    for (org.springframework.validation.FieldError fe : ex.getBindingResult().getFieldErrors()) {
        Map<String, Object> err = new java.util.LinkedHashMap<>();
        err.put("field", fe.getField());
        err.put("rejectedValue", fe.getRejectedValue());
        err.put("message", fe.getDefaultMessage());
        errors.add(err);
    }
    Map<String, Object> body = new java.util.LinkedHashMap<>();
    body.put("timestamp", java.time.Instant.now().toString());
    body.put("status", 400);
    body.put("error", "Bad Request");
    body.put("message", "validation failed");
    body.put("path", request.getRequestURI());
    body.put("errors", errors);
    return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(body);
}
```

### Task 4.5: Add the validation e2e test

**Files:**
- Modify: `src/test/java/com/uditgoel/groomify/AuthFlowEndToEndTest.java`

- [ ] **Step 1: Add a new test**

```java
@Test
void signupWithMalformedPayload_returns400_withFieldErrors() {
    Map<String, Object> body = new HashMap<>();
    body.put("username", "");                      // @NotBlank violated
    body.put("email", "not-an-email");             // @Email violated
    body.put("password", "weak");                  // @Pattern + @Size violated
    body.put("name", "");
    body.put("regId", "");
    body.put("govtIdType", "");
    body.put("govtId", "");
    body.put("contact", 9876543210L);
    body.put("gender", "");
    body.put("dob", "1990-01-15");
    body.put("billingAddress", Map.of("country", "India", "state", "X", "city", "X",
            "postalCode", 1, "address1", "X"));
    body.put("sameShipping", true);

    ResponseEntity<String> resp = rest.postForEntity(url("/api/auth/customer/signup"), body, String.class);

    assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(resp.getBody()).contains("\"errors\":");
    assertThat(resp.getBody()).contains("\"field\":\"email\"");
    assertThat(resp.getBody()).contains("\"field\":\"password\"");
}
```

- [ ] **Step 2: Run the new test**

Run: `mvn -Dtest=AuthFlowEndToEndTest#signupWithMalformedPayload_returns400_withFieldErrors test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 1, Failures: 0, Errors: 0`, `BUILD SUCCESS`.

If 500 instead of 400: the `MethodArgumentNotValidException` handler isn't registered. Verify Task 4.4 import + annotation.
If 200 (signup accepted): `@Valid` is missing from the controller method. Re-check Task 4.3.

- [ ] **Step 3: Run full suite**

Run: `mvn test 2>&1 | grep -E "Tests run:|BUILD" | head -3`
Expected: `Tests run: 21` (was 20, +1), `BUILD SUCCESS`.

### Task 4.6: Commit Item #4

- [ ] **Step 1: Commit**

```bash
git add src/main/java/com/uditgoel/groomify/dto \
        src/main/java/com/uditgoel/groomify/controller \
        src/main/java/com/uditgoel/groomify/exception/GlobalExceptionHandler.java \
        src/test/java/com/uditgoel/groomify/AuthFlowEndToEndTest.java
git -c commit.gpgsign=false commit -m "$(cat <<'EOF'
P0 #4: Bean Validation on every input DTO

- @NotBlank / @Email / @Pattern / @Size / @NotNull / @Past / @DecimalMin
  on CustomerDTO + parent + EmployeeInput + parent.
- @Valid on every @RequestBody across 5 controllers.
- GlobalExceptionHandler maps MethodArgumentNotValidException to 400 with
  field-level errors[] (field / rejectedValue / message).
- New e2e: malformed signup → 400 + body lists email + password fields.

Tests: 21/21 green.
EOF
)"
```

---

## Phase 99 — Push and open PR

### Task 99.1: Push and open the PR

- [ ] **Step 1: Push the branch**

Run: `git push -u origin chore/p0-prod-readiness`
Expected: New branch on origin with the four commits.

- [ ] **Step 2: Open the PR via gh**

Run:
```bash
gh pr create --base master --head chore/p0-prod-readiness \
  --title "P0 production-readiness: Flyway, Actuator, refresh-token rotation, Bean Validation" \
  --body "$(cat <<'EOF'
## Summary

Lands the four P0 items from `docs/superpowers/specs/2026-05-02-p0-p3-production-readiness-design.md`:

1. **Flyway** replaces \`ddl-auto=update\` with \`validate\`. \`V1__baseline.sql\` captured from the
   Hibernate-generated schema. \`baseline-on-migrate=true\` so existing dev volumes work.
2. **Actuator + Prometheus + JSON-structured logging** — \`/actuator/health\`, \`/actuator/info\`,
   \`/actuator/prometheus\` exposed; SecurityConfig permits them; logback-spring.xml uses
   plain encoder for default/dev/test profiles, Logstash JSON encoder for prod.
3. **Refresh-token rotation with reuse detection** — every \`/refreshToken\` call rotates;
   reuse of an already-rotated token wipes the entire family. Backed by a new
   \`RefreshTokenRecord\` (subject, familyId, rotatedAt) and per-family Redis Sets.
4. **Bean Validation** — \`@Valid @RequestBody\` everywhere; \`MethodArgumentNotValidException\`
   handled in \`GlobalExceptionHandler\` returning 400 with field-level errors[].

## Test plan

- [x] \`mvn test\` → 21/21 green (was 15; +6 new tests across the 4 items)
- [x] Flyway baseline matches Hibernate-generated schema (validated in CI on first migrate)
- [x] Actuator endpoints reachable without auth, return 200
- [x] Refresh-token rotation: old token rejected post-rotate; reuse wipes family
- [x] Malformed signup → 400 with field-level error list
EOF
)"
```

Expected: gh prints the PR URL.

- [ ] **Step 3: Confirm PR is open**

Run: `gh pr view --json state,url`
Expected: `state: OPEN`, URL printed.

---

## Self-review checklist

After execution, before marking the plan done:

- [ ] All 21 expected tests run on `mvn test` and pass.
- [ ] No `ddl-auto=update` left anywhere in main resources (only `validate` for prod, `create-drop` for tests).
- [ ] No `getJwtJsonSubjectKeyFromRefreshToken` references remain — was fully replaced by `rotateRefreshToken`.
- [ ] `/actuator/prometheus` returns Prometheus text format with at least `jvm_memory_used_bytes`.
- [ ] Bean Validation: a curl with `--data '{"username":""}'` to signup returns 400, not 500.
- [ ] PR description references the spec doc by relative path.
- [ ] No leftover TODO/FIXME comments in the diff.

If any item fails — STOP, fix, recommit, push.

---

## Rollback procedure

If anything in this PR breaks production after merge:
- `git revert <merge-commit-sha>` on master and force-push (or via PR).
- Specifically for Flyway: rolling back the `validate` config to `update` is enough — Flyway's
  bookkeeping table will be ignored if Flyway is disabled. The `V1__baseline.sql` is
  idempotent against the Hibernate-generated schema.
