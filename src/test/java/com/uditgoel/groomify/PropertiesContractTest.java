package com.uditgoel.groomify;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.Test;

/**
 * Pin the production-config contract so accidental reverts get caught at test time.
 * The Flyway / Actuator / refresh-rotation work makes specific configuration choices
 * that are easy to undo (e.g. `ddl-auto=validate` → `update` is a one-character edit
 * that silently re-enables Hibernate's auto-DDL). This test fails fast if any of
 * those choices regresses.
 */
class PropertiesContractTest {

    @Test
    void productionPropertiesPinSchemaOwnership() throws Exception {
        Properties props = loadMainProperties();

        assertThat(props.getProperty("spring.jpa.hibernate.ddl-auto"))
                .as("Hibernate must NOT own DDL in prod; Flyway does")
                .isEqualTo("validate");
        assertThat(props.getProperty("spring.flyway.enabled"))
                .as("Flyway must be enabled in prod")
                .isEqualTo("true");
        assertThat(props.getProperty("spring.flyway.baseline-on-migrate"))
                .as("baseline-on-migrate=true is required so existing dev volumes don't reject V1")
                .isEqualTo("true");
        assertThat(props.getProperty("spring.flyway.locations"))
                .isEqualTo("classpath:db/migration");
    }

    private Properties loadMainProperties() throws Exception {
        Properties props = new Properties();
        try (InputStream in = getClass().getResourceAsStream("/application.properties")) {
            assertThat(in).as("application.properties must be on the test classpath").isNotNull();
            props.load(in);
        }
        return props;
    }
}
