package com.niit.lookatme.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.services.Service;
import com.niit.lookatme.dto.AddressInput;
import com.niit.lookatme.dto.ServiceExtDTO;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;

public class CustomerAndEmployeeUtils {

	private static final String ENCRYPTIONKEY = "ABCDEFGHIJKLMNOP";
	private static final String CIPHERTRANSFORMATION = "AES/CBC/PKCS5PADDING";
	private static final String AESENCRYPTIONALGORITHEM = "AES";

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerAndEmployeeUtils.class);

	private CustomerAndEmployeeUtils() {
	}

	public static AddressInput populateAddressOut(Address address) {
		AddressInput addressInput = new AddressInput(address.getAddress1(), address.getAddress2(),
				address.getAddress3(), address.getState(), address.getCity(), address.getRegion(),
				address.getPostalCode());
		addressInput.setCountry(address.getCountry());
		return addressInput;
	}

	public static ServiceExtDTO populateServiceExtDTO(Service service) {
		return new ServiceExtDTO(service.getServiceGroup().getName(), service.getHsn(), service.getName(),
				service.getPrice(), Converter.dateToLocalTime(service.getTime()), service.getIsActive());
	}

	public static Address populateAddressObject(AddressInput addressInput) {
		Address currentAddress = new Address();
		currentAddress.setAddress1(addressInput.getAddress1());
		currentAddress.setAddress2(addressInput.getAddress2());
		currentAddress.setAddress3(addressInput.getAddress3());
		currentAddress.setCity(addressInput.getCity());
		currentAddress.setRegion(addressInput.getRegion());
		currentAddress.setState(addressInput.getState());
		currentAddress.setCountry(addressInput.getCountry());
		currentAddress.setPostalCode(addressInput.getPostalCode());
		return currentAddress;
	}

	public static String createRegId(String userType, String... username) {
		StringBuilder strBuilder = new StringBuilder(userType)
				.append(DateTimeFormatter.ofPattern("yyyymmddHHmmss").format(LocalDateTime.now()));
		for (String usrname : username)
			strBuilder.append(usrname);
		return strBuilder.toString();
	}

	public static String createImageAndFetchUrl(UserType userType, UserImageInputType userImageInputType,
			MultipartFile pictureFile, String username) {
		if (Optional.ofNullable(pictureFile).map(MultipartFile::getSize).map(x -> Boolean.valueOf(x > 0))
				.orElse(false)) {
			String filePathName = uploadPictureImage(userType, pictureFile, username, userImageInputType);
			if (!StringUtils.isEmpty(filePathName)) {
				return filePathName;
			}
		}
		return null;
	}

	public static String uploadPictureImage(UserType userType, MultipartFile file, String empNo,
			UserImageInputType userImageInputType) {
		try {
			byte[] fileBytes = file.getBytes();
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

			StringBuilder builder = new StringBuilder(userType.toString().toLowerCase()).append("/").append(empNo)
					.append("/").append(userImageInputType.toString()).append("/");

			Path directoryPath = Paths.get(builder.toString());
			String fileRelativePathName = builder.append(empNo).append('_').append(userImageInputType.toString())
					.append('.').append(fileExtension).toString();
			if (!directoryPath.toFile().exists()) {
				Files.createDirectories(directoryPath);
				Files.write(Paths.get(fileRelativePathName), fileBytes);
				return fileRelativePathName;
			}
			if (Files.deleteIfExists(Paths.get(fileRelativePathName))) {
				Files.write(Paths.get(fileRelativePathName), fileBytes);
				return fileRelativePathName;
			}
		} catch (IOException ex) {
			LOGGER.error(ex.getMessage());
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
		try {
			Cipher cipher = Cipher.getInstance(CIPHERTRANSFORMATION);
			byte[] key = ENCRYPTIONKEY.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKey = new SecretKeySpec(key, AESENCRYPTIONALGORITHEM);
			IvParameterSpec ivparameterspec = new IvParameterSpec(key);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
			byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
			Base64.Encoder encoder = Base64.getEncoder();
			encryptedText = encoder.encodeToString(cipherText);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | InvalidAlgorithmParameterException e) {
			LOGGER.error(e.getMessage());
		}
		return encryptedText;
	}

	public static List<String> getSplittedNameArr(String name) {
		name = name.trim();
		List<String> arrList = new ArrayList<>(3);
		if (name.contains(" ")) {
			String[] nameArr = name.split("\\s+", 3);
			arrList.add(nameArr[0]);
			if (nameArr.length > 2) {
				arrList.add(nameArr[1]);
				arrList.add(nameArr[2]);
			} else {
				arrList.add(nameArr[1]);
			}
		} else {
			arrList.add(name);
		}
		return arrList;
	}

}
