package com.uditgoel.groomify.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

	private static final String ENCRYPTIONKEY = "ABCDEFGHIJKLMNOP";
	private static final String CIPHERTRANSFORMATION = "AES/CBC/PKCS5PADDING";
	private static final String AESENCRYPTIONALGORITHEM = "AES";

	private static boolean isCustomerPasswordExpire;
	private static int addCustomerPasswordExpiryDays;
	private static boolean isEmployeePasswordExpire;
	private static int addEmployeePasswordExpiryDays;

	private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);

	public AppUtils() {
		super();
	}

	@Value("${customer.password.expiry.policy}")
	public void setCustomerPasswordExpire(boolean isCustomerPasswordExpire) {
		AppUtils.isCustomerPasswordExpire = isCustomerPasswordExpire;
	}

	@Value("${customer.password.expiry.policy.days}")
	public void setAddCustomerPasswordExpiryDays(int addCustomerPasswordExpiryDays) {
		AppUtils.addCustomerPasswordExpiryDays = addCustomerPasswordExpiryDays;
	}

	@Value("${employee.password.expiry.policy}")
	public void setEmployeePasswordExpire(boolean isEmployeePasswordExpire) {
		AppUtils.isEmployeePasswordExpire = isEmployeePasswordExpire;
	}

	@Value("${employee.password.expiry.policy.days}")
	public void setAddEmployeePasswordExpiryDays(int addEmployeePasswordExpiryDays) {
		AppUtils.addEmployeePasswordExpiryDays = addEmployeePasswordExpiryDays;
	}

	public static Date getEmployeeExpirationDateFromCurrent(Calendar cal) {
		if (isEmployeePasswordExpire) {
			cal.add(Calendar.DATE, addEmployeePasswordExpiryDays);
			return cal.getTime();
		}
		return null;
	}

	public static Date getCustomerExpirationDateFromCurrent(Calendar cal) {
		if (isCustomerPasswordExpire) {
			cal.add(Calendar.DATE, addCustomerPasswordExpiryDays);
			return cal.getTime();
		}
		return null;
	}

	public static String decrypt(String encryptedText) {
		String decryptedText = "";
		try {
			Cipher cipher = Cipher.getInstance(CIPHERTRANSFORMATION);
			byte[] key = ENCRYPTIONKEY.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKey = new SecretKeySpec(key, AESENCRYPTIONALGORITHEM);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] cipherText = decoder.decode(encryptedText.getBytes(StandardCharsets.UTF_8));
			decryptedText = new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			LOGGER.error(e.getMessage());
		}
		return decryptedText;
	}

	public static String encrypt(String plainText) {
		String encryptedText = "";
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHERTRANSFORMATION);
			byte[] key = ENCRYPTIONKEY.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKey = new SecretKeySpec(key, AESENCRYPTIONALGORITHEM);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			Base64.Encoder encoder = Base64.getEncoder();
			encryptedText = encoder.encodeToString(cipherText);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.error(e.getMessage());
		}

		return encryptedText;
	}

}
