package com.uditgoel.groomify.support;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;

import redis.embedded.RedisServer;

/**
 * ApplicationContextInitializer that:
 * <ul>
 * <li>Spawns an in-process Redis server on a free port (one per JVM, reused
 * across tests).</li>
 * <li>Generates an RSA keypair on first use and exposes it as
 * {@code rsa.public.key} /
 * {@code rsa.private.key} (Base64-encoded).</li>
 * </ul>
 * Used via
 * {@code @ContextConfiguration(initializers = EmbeddedRedisInitializer.class)}.
 */
public class EmbeddedRedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static volatile RedisServer redisServer;
	private static volatile int redisPort;
	private static final String RSA_PUBLIC;
	private static final String RSA_PRIVATE;

	static {
		try {
			KeyPair kp = KeyPairGenerator.getInstance("RSA").generateKeyPair();
			RSA_PUBLIC = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
			RSA_PRIVATE = Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded());
		} catch (NoSuchAlgorithmException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ensureRedisRunning();
		applicationContext.getEnvironment().getPropertySources()
				.addFirst(new MapPropertySource("embedded-redis", java.util.Map.of(
						"datasource.redis.master.host", "127.0.0.1",
						"datasource.redis.master.port", redisPort,
						"datasource.redis.master.pass", "",
						"datasource.redis.slave.host", "127.0.0.1",
						"datasource.redis.slave.port", redisPort,
						"datasource.redis.slave.pass", "",
						"rsa.public.key", RSA_PUBLIC,
						"rsa.private.key", RSA_PRIVATE)));
	}

	private static synchronized void ensureRedisRunning() {
		if (redisServer != null) {
			return;
		}
		try {
			redisPort = freePort();
			redisServer = RedisServer.newRedisServer().port(redisPort).build();
			redisServer.start();
		} catch (IOException e) {
			throw new IllegalStateException("Could not start embedded Redis", e);
		}
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				redisServer.stop();
			} catch (IOException ignored) {
				// best-effort shutdown
			}
		}));
	}

	private static int freePort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0)) {
			return socket.getLocalPort();
		}
	}
}
