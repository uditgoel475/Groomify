package com.uditgoel.groomify.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

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

import jakarta.annotation.PostConstruct;

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
	private static final int HS512_REQUIRED_BYTES = 64;

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private long jwtExpirationInMs;

	@Value("${app.jwt.refresh.token.length}")
	private int refreshTokenLength;

	@Value("${app.jwt.refresh.expirationInMs}")
	private long refreshTokenExpirationInMs;

	private final ObjectMapper objectMapper;

	private final RedisHelper redisHelper;

	private SecretKey signingKey;

	public JwtTokenProvider(ObjectMapper objectMapper, RedisHelper redisHelper) {
		this.objectMapper = objectMapper;
		this.redisHelper = redisHelper;
	}

	@PostConstruct
	void validateAndCacheSigningKey() {
		if (jwtSecret == null) {
			throw new IllegalStateException("app.jwtSecret is not set");
		}
		byte[] secretBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
		if (secretBytes.length < HS512_REQUIRED_BYTES) {
			throw new IllegalStateException(
					"app.jwtSecret must be at least " + HS512_REQUIRED_BYTES + " bytes for HS512 (got "
							+ secretBytes.length + " bytes)");
		}
		signingKey = Keys.hmacShaKeyFor(secretBytes);
	}

	public JwtAuthenticationResponse generateToken(Authentication authentication, UserType userType)
			throws JsonProcessingException {

		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		String refreshToken = createRefreshToken(userPrincipal.getUsername(), userType.toString());

		JwtJsonSubjectKey subjectKey = new JwtJsonSubjectKey(userPrincipal.getId(), userPrincipal.getUsername(),
				userPrincipal.getEmail(), userType);
		if (redisHelper.isRedisWorking()) {
			redisHelper.createRedisJWTRefreshToken(refreshToken, subjectKey);
		}
		return issueAccessToken(subjectKey, refreshToken);
	}

	public JwtAuthenticationResponse createAccessToken(JwtJsonSubjectKey subjectKey, String refreshToken)
			throws JsonProcessingException {
		return issueAccessToken(subjectKey, refreshToken);
	}

	private JwtAuthenticationResponse issueAccessToken(JwtJsonSubjectKey subjectKey, String refreshToken)
			throws JsonProcessingException {
		Instant now = Instant.now();
		Instant expire = now.plusMillis(jwtExpirationInMs);
		String token = Jwts.builder()
				.subject(AppUtils.encrypt(objectMapper.writeValueAsString(subjectKey)))
				.issuedAt(Date.from(now))
				.expiration(Date.from(expire))
				.signWith(signingKey, Jwts.SIG.HS512)
				.compact();
		return new JwtAuthenticationResponse(token, refreshToken, expire);
	}

	/**
	 * Resolve a refresh token to its subject payload.
	 *
	 * @return empty if the token is missing/unknown; throws if Redis itself is unavailable
	 *         (a 500-class server condition rather than an auth failure).
	 */
	public Optional<JwtJsonSubjectKey> getJwtJsonSubjectKeyFromRefreshToken(String refreshToken) {
		if (refreshToken == null || refreshToken.isBlank()) {
			return Optional.empty();
		}
		if (!redisHelper.isRedisWorking()) {
			throw new IllegalStateException("Redis unavailable; cannot resolve refresh token");
		}
		try {
			return Optional.ofNullable(redisHelper.getRefreshTokenDetails(refreshToken));
		} catch (IOException e) {
			logger.error("Could not deserialize refresh token payload: {}", e.getMessage());
			return Optional.empty();
		}
	}

	private String createRefreshToken(String username, String userType) {
		SecureTextRandomProvider secureTextRandomProvider = new SecureTextRandomProvider();
		RandomStringGenerator refreshTokenGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
				.usingRandom(secureTextRandomProvider).build();
		String tokenPayload = refreshTokenGenerator.generate(refreshTokenLength)
				+ "/username/" + username
				+ "/usertype/" + userType;
		try {
			return RSAEncryptUtil.encrypt(tokenPayload);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Could not generate refresh token", e);
		}
	}

	/**
	 * Extract subject from a signed access token. Pure signature + payload decode — no Redis.
	 */
	public JwtJsonSubjectKey getUserIdFromJWT(String token) throws IOException {
		Claims claims = Jwts.parser()
				.verifyWith(signingKey)
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
			Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(authToken);
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
