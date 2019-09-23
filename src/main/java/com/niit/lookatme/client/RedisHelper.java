package com.niit.lookatme.client;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.lookatme.dto.JwtJsonSubjectKey;

@Component("redisHelper")
public class RedisHelper {

	@Value("${app.jwtExpirationInMs}")
	private long jwtExpirationInMs;

	@Value("${app.jwt.refresh.expirationInMs}")
	private long refreshTokenExpirationInMs;

	@Value("${jwt.refresh.token.key.format}")
	private String jwtRefreshTokenKey;

	@Value("${jwt.access.token.key.format}")
	private String jwtAccessTokenKey;

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

	public void createRedisJwtAccessToken(String token, JwtJsonSubjectKey unencryptedValue)
			throws JsonProcessingException {
		String key = String.format(jwtAccessTokenKey, token);
		redisClient.setValue(key, objectMapper.writeValueAsString(unencryptedValue), jwtExpirationInMs,
				TimeUnit.MILLISECONDS);
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

	public JwtJsonSubjectKey getAccessTokenDetails(String token) throws IOException {
		String key = String.format(jwtAccessTokenKey, token);
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
