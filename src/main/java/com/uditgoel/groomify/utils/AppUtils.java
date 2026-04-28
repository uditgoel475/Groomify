package com.uditgoel.groomify.utils;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * AES-GCM helper for the JWT subject payload, plus password-expiry policy
 * values.
 *
 * <p>
 * The encryption key is sourced from {@code app.aes.key} (Base64-encoded
 * 16/24/32 bytes).
 * Each call generates a fresh 12-byte IV which is prepended to the ciphertext.
 * GCM gives
 * authenticated encryption — tampering with ciphertext fails on decrypt instead
 * of returning
 * garbage. The previous version used CBC with a fixed key reused as IV, which
 * leaks plaintext
 * equality.
 *
 * <p>
 * Static-field bootstrapping in {@link #init()} is intentional: callers (e.g.
 * {@code JwtTokenProvider}) use the static API. The write happens exactly once
 * via Spring's
 * {@code @PostConstruct} contract.
 */
@Component
@SuppressWarnings("java:S2696")
public class AppUtils {

	private static final String CIPHER_TRANSFORMATION = "AES/GCM/NoPadding";
	private static final String AES_ALG = "AES";
	private static final int IV_LENGTH = 12;
	private static final int TAG_LENGTH_BITS = 128;
	private static final SecureRandom RNG = new SecureRandom();

	private static byte[] aesKey;
	private static boolean customerPasswordExpiryEnabled;
	private static int customerPasswordExpiryDays;
	private static boolean employeePasswordExpiryEnabled;
	private static int employeePasswordExpiryDays;

	@Value("${app.aes.key:}")
	private String injectedAesKey;

	@Value("${customer.password.expiry.policy}")
	private boolean injectedCustomerExpiry;

	@Value("${customer.password.expiry.policy.days}")
	private int injectedCustomerExpiryDays;

	@Value("${employee.password.expiry.policy}")
	private boolean injectedEmployeeExpiry;

	@Value("${employee.password.expiry.policy.days}")
	private int injectedEmployeeExpiryDays;

	@PostConstruct
	void init() {
		if (injectedAesKey == null || injectedAesKey.isBlank()) {
			throw new IllegalStateException(
					"app.aes.key is not set; configure a Base64-encoded 16/24/32-byte AES key");
		}
		byte[] decoded = Base64.getDecoder().decode(injectedAesKey);
		if (decoded.length != 16 && decoded.length != 24 && decoded.length != 32) {
			throw new IllegalStateException(
					"app.aes.key must decode to 16, 24, or 32 bytes (got " + decoded.length + ")");
		}
		aesKey = decoded;
		customerPasswordExpiryEnabled = injectedCustomerExpiry;
		customerPasswordExpiryDays = injectedCustomerExpiryDays;
		employeePasswordExpiryEnabled = injectedEmployeeExpiry;
		employeePasswordExpiryDays = injectedEmployeeExpiryDays;
	}

	public static Date getEmployeeExpirationDateFromCurrent(Calendar cal) {
		if (employeePasswordExpiryEnabled) {
			cal.add(Calendar.DATE, employeePasswordExpiryDays);
			return cal.getTime();
		}
		return null;
	}

	public static Date getCustomerExpirationDateFromCurrent(Calendar cal) {
		if (customerPasswordExpiryEnabled) {
			cal.add(Calendar.DATE, customerPasswordExpiryDays);
			return cal.getTime();
		}
		return null;
	}

	public static String encrypt(String plainText) {
		try {
			byte[] iv = new byte[IV_LENGTH];
			RNG.nextBytes(iv);
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, AES_ALG),
					new GCMParameterSpec(TAG_LENGTH_BITS, iv));
			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			byte[] out = new byte[iv.length + cipherText.length];
			System.arraycopy(iv, 0, out, 0, iv.length);
			System.arraycopy(cipherText, 0, out, iv.length, cipherText.length);
			return Base64.getEncoder().encodeToString(out);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("AES encrypt failed", e);
		}
	}

	public static String decrypt(String encryptedText) {
		try {
			byte[] data = Base64.getDecoder().decode(encryptedText);
			if (data.length <= IV_LENGTH) {
				throw new IllegalStateException("AES ciphertext too short");
			}
			byte[] iv = Arrays.copyOfRange(data, 0, IV_LENGTH);
			byte[] cipherText = Arrays.copyOfRange(data, IV_LENGTH, data.length);
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(aesKey, AES_ALG),
					new GCMParameterSpec(TAG_LENGTH_BITS, iv));
			return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("AES decrypt failed", e);
		}
	}

}
