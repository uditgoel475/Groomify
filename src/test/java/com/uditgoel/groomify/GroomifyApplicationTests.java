package com.uditgoel.groomify;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class GroomifyApplicationTests {

	@Test
	void applicationClassLoads() {
		// Placeholder test. A real @SpringBootTest + Testcontainers harness
		// would start Postgres + Redis and exercise the auth flow.
		assertNotNull(GroomifyApplication.class);
	}
}
