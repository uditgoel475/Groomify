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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

        registry.add("app.jwtSecret", () -> "test-secret-test-secret-test-secret-test-secret-test-secret-1234");
        registry.add("app.aes.key", () -> "AAECAwQFBgcICQoLDA0ODxAREhMUFRYXGBkaGxwdHh8=");
        KeyPair kp = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        registry.add("rsa.public.key", () -> Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
        registry.add("rsa.private.key", () -> Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));

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
                "SELECT count(*) FROM information_schema.tables "
                        + "WHERE table_schema = 'public' AND table_name = 'customer'",
                Integer.class);
        assertThat(customerTableExists).isEqualTo(1);
    }
}
