package com.uditgoel.groomify.security;

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
import com.uditgoel.groomify.client.RedisHelper;
import com.uditgoel.groomify.dto.JwtAuthenticationResponse;
import com.uditgoel.groomify.dto.JwtJsonSubjectKey;
import com.uditgoel.groomify.dto.UserType;
import com.uditgoel.groomify.utils.AppUtils;
import com.uditgoel.groomify.utils.RSAEncryptUtil;

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

	public JwtAuthenticationResponse generateToken(Authentication authentication, UserType userType)
			throws JsonProcessingException {

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String refreshToken = createRefreshToken(userPrincipal.getUsername(), userType.toString());

		JwtJsonSubjectKey jwtJsonSubjectKey = new JwtJsonSubjectKey(userPrincipal.getId(), userPrincipal.getUsername(),
				userPrincipal.getEmail(), userType);
		JwtAuthenticationResponse jwtAuthenticationResponse = getAccessToken(jwtJsonSubjectKey);
		if (redisHelper.isRedisWorking()) {
			redisHelper.createRedisJWTRefreshToken(refreshToken, jwtJsonSubjectKey);
			redisHelper.createRedisJwtAccessToken(jwtAuthenticationResponse.getAccessToken(), jwtJsonSubjectKey);
		}
		jwtAuthenticationResponse.setRefreshToken(refreshToken);
		return jwtAuthenticationResponse;
	}

	public JwtAuthenticationResponse createAccessToken(JwtJsonSubjectKey jwtJsonSubjectKey)
			throws JsonProcessingException {
		JwtAuthenticationResponse jwtAuthenticationResponse = getAccessToken(jwtJsonSubjectKey);
		if (redisHelper.isRedisWorking()) {
			redisHelper.createRedisJwtAccessToken(jwtAuthenticationResponse.getAccessToken(), jwtJsonSubjectKey);
		}
		return jwtAuthenticationResponse;
	}

	private JwtAuthenticationResponse getAccessToken(JwtJsonSubjectKey jwtJsonSubjectKey)
			throws JsonProcessingException {
		Date now = Calendar.getInstance().getTime();
		Date expire = new Date(now.getTime() + jwtExpirationInMs);
		return new JwtAuthenticationResponse(
				Jwts.builder().setSubject(AppUtils.encrypt(objectMapper.writeValueAsString(jwtJsonSubjectKey)))
						.setHeaderParam(Header.TYPE, "JWT").setIssuedAt(now).setExpiration(expire)
						.signWith(SignatureAlgorithm.HS512, jwtSecret).compact(),
				expire);
	}

	public JwtJsonSubjectKey getJwtJsonSubjectKeyFromRefreshToken(String refreshToken) {
		if (redisHelper.isRedisWorking()) {
			try {
				return redisHelper.getRefreshTokenDetails(refreshToken);
			} catch (IOException e) {
				logger.error(String.format("couldn't get refresh token details : %s", e.getMessage()));
			}
		}
		return null;
	}

	private String createRefreshToken(String username, String userType) {
		SecureTextRandomProvider secureTextRandomProvider = new SecureTextRandomProvider();
		RandomStringGenerator refreshTokenGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
				.usingRandom(secureTextRandomProvider).build();
		try {
			StringBuilder refreshTokenBuilder = new StringBuilder(refreshTokenGenerator.generate(refreshTokenLength));
			refreshTokenBuilder.append("/username/").append(username).append("/usertype/").append(userType);
			
			String encryptedToken = RSAEncryptUtil.encrypt(refreshTokenBuilder.toString());
			if(null != getJwtJsonSubjectKeyFromRefreshToken(encryptedToken)) {
				redisHelper.getRedisClient().delete(encryptedToken);
			}
			return encryptedToken;
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {
			logger.error(String.format("couldn't generate Refresh Token : %s", e.getMessage()));
		}
		return null;
	}

	public JwtJsonSubjectKey getUserIdFromJWT(String token) throws IOException {
		if (redisHelper.isRedisWorking()) {
			return getAccessTokenDetails(token);
		}
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return objectMapper.readValue(AppUtils.decrypt(claims.getSubject()), JwtJsonSubjectKey.class);
	}

	private JwtJsonSubjectKey getAccessTokenDetails(String token) {
		try {
			return redisHelper.getAccessTokenDetails(token);
		} catch (IOException e) {
			logger.error("Invalid JWT Access Token");
		}
		return null;
	}

	public boolean validateAccessToken(String authToken) {
		if (redisHelper.isRedisWorking()) {
			return null != getAccessTokenDetails(authToken);
		}
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
