package com.uditgoel.groomify.client;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uditgoel.groomify.security.RefreshTokenRecord;

/**
 * Redis token store — only refresh tokens are persisted. Access tokens are stateless,
 * validated by signature on every request.
 */
@Component("redisHelper")
public class RedisHelper {

	@Value("${app.jwt.refresh.expirationInMs}")
	private long refreshTokenExpirationInMs;

	@Value("${jwt.refresh.token.key.format}")
	private String jwtRefreshTokenKey;

	private final RedisClient redisClient;

	private final ObjectMapper objectMapper;

	private boolean redisWorking;

	public RedisHelper(RedisClient redisClient, ObjectMapper objectMapper) {
		this.redisClient = redisClient;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	public void init() {
		try {
			redisWorking = Optional.ofNullable(redisClient).map(RedisClient::getConnectionFactory)
					.map(RedisConnectionFactory::getConnection).map(RedisConnection::ping).filter("PONG"::equals)
					.isPresent();
		} catch (RuntimeException e) {
			// Redis unreachable at startup — treat as not working so the app still boots.
			// Refresh-token writes / reads will be skipped; access tokens still work.
			redisWorking = false;
		}
	}

	private String familyKey(String familyId) {
		return "refresh:family:" + familyId;
	}

	public void storeRefreshTokenWithFamily(String token, RefreshTokenRecord tokenRecord)
			throws JsonProcessingException {
		String key = String.format(jwtRefreshTokenKey, token);
		redisClient.setValue(key, objectMapper.writeValueAsString(tokenRecord),
				refreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
		String setKey = familyKey(tokenRecord.familyId());
		redisClient.sadd(setKey, token);
		// Family set tracks live tokens for atomic wipe on theft. Each new child resets
		// the family TTL, so the set lives as long as its longest-lived child — preventing
		// the set from leaking after all child tokens expire.
		redisClient.getRedisTemplate().expire(setKey, refreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
	}

	public RefreshTokenRecord getRefreshTokenRecord(String token) throws IOException {
		String key = String.format(jwtRefreshTokenKey, token);
		String value = redisClient.getValue(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return objectMapper.readValue(value, RefreshTokenRecord.class);
	}

	public void markRotated(String token, RefreshTokenRecord tokenRecord) throws JsonProcessingException {
		String key = String.format(jwtRefreshTokenKey, token);
		RefreshTokenRecord rotated = new RefreshTokenRecord(
				tokenRecord.subject(), tokenRecord.familyId(), Instant.now());
		// Deliberately writes a fresh TTL: the rotated parent stays in Redis as a
		// reuse-detection sentinel for the full refresh-token lifetime. A theft attempt
		// using the parent token any time within that window still triggers family wipe.
		redisClient.setValue(key, objectMapper.writeValueAsString(rotated),
				refreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
	}

	public void wipeFamily(String familyId) {
		String setKey = familyKey(familyId);
		Set<String> tokens = redisClient.smembers(setKey);
		if (tokens != null) {
			for (String t : tokens) {
				redisClient.delete(String.format(jwtRefreshTokenKey, t));
			}
		}
		redisClient.delete(setKey);
	}

	public boolean isRedisWorking() {
		return redisWorking;
	}

	public RedisClient getRedisClient() {
		return redisClient;
	}
}
