package com.uditgoel.groomify.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.spec.AlgorithmParameterSpec;

/**
 * RSA helper for encrypting the opaque refresh-token payload before it's stored
 * in Redis.
 * Keys are loaded from {@code rsa.public.key} / {@code rsa.private.key} (Base64
 * X.509 / PKCS#8).
 * Uses OAEP (SHA-256 / MGF1) padding.
 */
@Component
public class RSAEncryptUtil {

	private static final Logger logger = LoggerFactory.getLogger(RSAEncryptUtil.class);
	private static final String ALGO = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";

	private static String publicKeyBase64;
	private static String privateKeyBase64;

	@Value("${rsa.public.key}")
	private String injectedPublicKey;

	@Value("${rsa.private.key}")
	private String injectedPrivateKey;

	@PostConstruct
	void init() {
		publicKeyBase64 = injectedPublicKey;
		privateKeyBase64 = injectedPrivateKey;
	}

	private static AlgorithmParameterSpec oaepSpec() {
		return new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT);
	}

	private static PublicKey getPublicKey(String base64PublicKey) {
		try {
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Invalid Public Key Algorithm used: {}", e.getMessage());
		} catch (InvalidKeySpecException e) {
			logger.error("Invalid Public Key specification used: {}", e.getMessage());
		}
		return null;
	}

	private static PrivateKey getPrivateKey(String base64PrivateKey) {
		try {
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
					Base64.getDecoder().decode(base64PrivateKey.getBytes()));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Invalid Private Key Algorithm used: {}", e.getMessage());
		} catch (InvalidKeySpecException e) {
			logger.error("Invalid Private Key specification used: {}", e.getMessage());
		}
		return null;
	}

	public static String encrypt(String data) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
		try {
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyBase64), oaepSpec());
			return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
		} catch (java.security.InvalidAlgorithmParameterException e) {
			throw new IllegalStateException("Invalid OAEP params", e);
		}
	}

	public static String decrypt(String data) throws IllegalBlockSizeException, InvalidKeyException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		try {
			Cipher cipher = Cipher.getInstance(ALGO);
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyBase64), oaepSpec());
			return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes())));
		} catch (java.security.InvalidAlgorithmParameterException e) {
			throw new IllegalStateException("Invalid OAEP params", e);
		}
	}
}
