package com.niit.lookatme.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.lookatme.dto.JwtJsonSubjectKey;
import com.niit.lookatme.dto.UserType;

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

	public void createRedisJwtAccessToken(String username, UserType userType, String token,
			JwtJsonSubjectKey unencryptedValue) throws JsonProcessingException {
		String key = String.format(jwtAccessTokenKey, userType.toString(), username, token);
		redisClient.setValue(key, objectMapper.writeValueAsString(unencryptedValue), jwtExpirationInMs,
				TimeUnit.MILLISECONDS);
	}

	public void createRedisJWTRefreshToken(String username, UserType userType, String token, String unencryptedValue) {
		String key = String.format(jwtRefreshTokenKey, userType.toString(), username, token);
		redisClient.setValue(key, unencryptedValue, refreshTokenExpirationInMs, TimeUnit.MILLISECONDS);
	}

	public String getEmployeeRefreshTokenDetails(String username, String token) {
		String key = String.format(jwtRefreshTokenKey, UserType.EMPLOYEE.toString(), username, token);
		return redisClient.getValue(key);
	}

	public JwtJsonSubjectKey getEmployeeAccessTokenDetails(String username, String token) throws IOException {
		String key = String.format(jwtAccessTokenKey, UserType.EMPLOYEE.toString(), username, token);
		return objectMapper.readValue(redisClient.getValue(key), JwtJsonSubjectKey.class);
	}

	public String getCustomerRefreshTokenDetails(String username, String token) {
		String key = String.format(jwtRefreshTokenKey, UserType.CUSTOMER.toString(), username, token);
		return redisClient.getValue(key);
	}

	public JwtJsonSubjectKey getCustomerAccessTokenDetails(String username, String token) throws IOException {
		String key = String.format(jwtAccessTokenKey, UserType.CUSTOMER.toString(), username, token);
		return objectMapper.readValue(redisClient.getValue(key), JwtJsonSubjectKey.class);
	}

}
