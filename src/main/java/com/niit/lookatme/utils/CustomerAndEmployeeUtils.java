package com.niit.lookatme.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dto.AddressInput;
import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dto.UserType;

public class CustomerAndEmployeeUtils {
	
	private CustomerAndEmployeeUtils() {}

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
	

	public static String createRegId(UserType userType, String username) {
		return userType.toString() + DateTimeFormatter.ofPattern("yyyymmddHHmmss").format(LocalDate.now()) + username;
	}
	
	public static String uploadPictureImage(UserType userType, MultipartFile file, String empNo, String typeName) {
		try {
			byte[] fileBytes = file.getBytes();
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

			StringBuilder builder = new StringBuilder(userType.toString().toLowerCase()).append("/").append(empNo).append("/" + typeName + "/");

			Path directoryPath = Paths.get(builder.toString());
			String fileRelativePathName = builder.append(empNo).append('_').append(typeName).append('.')
					.append(fileExtension).toString();
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
			// craete new inputfileempty exception
		}
		return null;
	}
}
