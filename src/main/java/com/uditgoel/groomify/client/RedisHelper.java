package com.uditgoel.groomify.client;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uditgoel.groomify.dto.JwtJsonSubjectKey;

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

	@Resource
	private RedisClient redisClient;

	@Resource
	private ObjectMapper objectMapper;

	private boolean redisWorking;

	@PostConstruct
	public void init() {
		redisWorking = Optional.ofNullable(redisClient).map(RedisClient::getConnectionFactory)
				.map(RedisConnectionFactory::getConnection).map(RedisConnection::ping).filter("PONG"::equals)
				.isPresent();
	}

	public void createRedisJWTRefreshToken(String token, JwtJsonSubjectKey unencryptedValue)
			throws JsonProcessingException {
		String key = String.format(jwtRefreshTokenKey, token);
		redisClient.setValue(key, objectMapper.writeValueAsString(unencryptedValue), refreshTokenExpirationInMs,
				TimeUnit.MILLISECONDS);
	}

	public JwtJsonSubjectKey getRefreshTokenDetails(String token) throws IOException {
		String key = String.format(jwtRefreshTokenKey, token);
		String redisValue = redisClient.getValue(key);
		if (StringUtils.isEmpty(redisValue))
			return null;
		return objectMapper.readValue(redisValue, JwtJsonSubjectKey.class);
	}

	public boolean isRedisWorking() {
		return redisWorking;
	}

	public RedisClient getRedisClient() {
		return redisClient;
	}
}
