package com.uditgoel.groomify.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import jakarta.annotation.Resource;

/**
 * JWT issuing and validation.
 *
 * <p>Design:
 * <ul>
 *   <li>Access tokens are stateless — validated purely by HS512 signature. No Redis round-trip on
 *       the hot path. The subject payload carries the user id / type, so no DB lookup either.</li>
 *   <li>Refresh tokens are opaque random strings, RSA-encrypted and stored in Redis keyed by
 *       the ciphertext. The refresh endpoint is the only place that touches Redis, which keeps
 *       server-side revocation while still serving the vast majority of requests without it.</li>
 * </ul>
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

	private SecretKey signingKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}

	public JwtAuthenticationResponse generateToken(Authentication authentication, UserType userType)
			throws JsonProcessingException {

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String refreshToken = createRefreshToken(userPrincipal.getUsername(), userType.toString());

		JwtJsonSubjectKey subjectKey = new JwtJsonSubjectKey(userPrincipal.getId(), userPrincipal.getUsername(),
				userPrincipal.getEmail(), userType);
		JwtAuthenticationResponse response = issueAccessToken(subjectKey);
		if (redisHelper.isRedisWorking()) {
			redisHelper.createRedisJWTRefreshToken(refreshToken, subjectKey);
		}
		response.setRefreshToken(refreshToken);
		return response;
	}

	public JwtAuthenticationResponse createAccessToken(JwtJsonSubjectKey subjectKey) throws JsonProcessingException {
		return issueAccessToken(subjectKey);
	}

	private JwtAuthenticationResponse issueAccessToken(JwtJsonSubjectKey subjectKey) throws JsonProcessingException {
		Date now = Calendar.getInstance().getTime();
		Date expire = new Date(now.getTime() + jwtExpirationInMs);
		String token = Jwts.builder()
				.subject(AppUtils.encrypt(objectMapper.writeValueAsString(subjectKey)))
				.issuedAt(now)
				.expiration(expire)
				.signWith(signingKey(), Jwts.SIG.HS512)
				.compact();
		return new JwtAuthenticationResponse(token, expire);
	}

	public JwtJsonSubjectKey getJwtJsonSubjectKeyFromRefreshToken(String refreshToken) {
		if (!redisHelper.isRedisWorking()) {
			logger.error("Redis unavailable; cannot resolve refresh token");
			return null;
		}
		try {
			return redisHelper.getRefreshTokenDetails(refreshToken);
		} catch (IOException e) {
			logger.error("Could not resolve refresh token: {}", e.getMessage());
			return null;
		}
	}

	private String createRefreshToken(String username, String userType) {
		SecureTextRandomProvider secureTextRandomProvider = new SecureTextRandomProvider();
		RandomStringGenerator refreshTokenGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
				.usingRandom(secureTextRandomProvider).build();
		try {
			StringBuilder refreshTokenBuilder = new StringBuilder(refreshTokenGenerator.generate(refreshTokenLength));
			refreshTokenBuilder.append("/username/").append(username).append("/usertype/").append(userType);

			String encryptedToken = RSAEncryptUtil.encrypt(refreshTokenBuilder.toString());
			if (redisHelper.isRedisWorking() && getJwtJsonSubjectKeyFromRefreshToken(encryptedToken) != null) {
				redisHelper.getRedisClient().delete(encryptedToken);
			}
			return encryptedToken;
		} catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException
				| NoSuchAlgorithmException e) {
			logger.error("Could not generate refresh token: {}", e.getMessage());
		}
		return null;
	}

	/**
	 * Extract subject from a signed access token. Pure signature + payload decode — no Redis.
	 */
	public JwtJsonSubjectKey getUserIdFromJWT(String token) throws IOException {
		Claims claims = Jwts.parser()
				.verifyWith(signingKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		return objectMapper.readValue(AppUtils.decrypt(claims.getSubject()), JwtJsonSubjectKey.class);
	}

	/**
	 * Validate an access token by signature and expiry only.
	 * Server-side revocation happens at the refresh endpoint (which does touch Redis).
	 */
	public boolean validateAccessToken(String authToken) {
		try {
			Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(authToken);
			return true;
		} catch (SignatureException ex) {
			logger.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			logger.error("Malformed JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty");
		}
		return false;
	}

	public long getRefreshTokenExpirationInMs() {
		return refreshTokenExpirationInMs;
	}
}
