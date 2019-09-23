package com.niit.lookatme.security;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.lookatme.client.RedisHelper;
import com.niit.lookatme.dto.JwtAuthenticationResponse;
import com.niit.lookatme.dto.JwtJsonSubjectKey;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.utils.AppUtils;
import com.niit.lookatme.utils.RSAEncryptUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * @author Konika
 */
@Component
public class JwtTokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private long jwtExpirationInMs;
	
	@Value("${app.jwt.refresh.token.length}")
	private int refreshTokenLength;
	
	@Value("${app.jwt.refresh.expirationInMs}")
	private long refreshTokenExpirationInMs;

	@Resource
	private ObjectMapper objectMapper;
	
	@Resource
	private RedisHelper redisHelper;

	public JwtAuthenticationResponse generateToken(Authentication authentication, UserType userType) throws JsonProcessingException {
		
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String refreshToken = generateEncryptedRefreshToken();
		
		redisHelper.createRedisJWTRefreshToken(userPrincipal.getUsername(), userType, refreshToken, "temp");

		Date now = Calendar.getInstance().getTime();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		JwtJsonSubjectKey jwtJsonSubjectKey = new JwtJsonSubjectKey(userPrincipal.getId(),
				userPrincipal.getUsername(), userPrincipal.getEmail(), userType);
		String accessToken = Jwts.builder()
				.setSubject(AppUtils.encrypt(objectMapper.writeValueAsString(jwtJsonSubjectKey))).setHeaderParam(Header.TYPE, "JWT")
				.setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, jwtSecret)
				.compact();
		redisHelper.createRedisJwtAccessToken(userPrincipal.getUsername(), userType, accessToken, jwtJsonSubjectKey);
		return new JwtAuthenticationResponse(accessToken, refreshToken, expiryDate);
	}

	private String generateEncryptedRefreshToken() {
		RandomStringGenerator refreshTokenGenerator = new RandomStringGenerator.Builder()
			      .withinRange(33, 45)
			      .build();
		try {
			String refreshToken = refreshTokenGenerator.generate(refreshTokenLength);
			return RSAEncryptUtil.encrypt(refreshToken);
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {
			logger.error(String.format("couldn't generate Refresh Token : %s", e.getMessage()));
		}
		return null;
	}

	public JwtJsonSubjectKey getUserIdFromJWT(String token) throws IOException {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		return objectMapper.readValue(AppUtils.decrypt(claims.getSubject()), JwtJsonSubjectKey.class);
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}
}
