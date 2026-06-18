package com.uditgoel.groomify.dto.customer;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerDTO extends CustomerOutDTO {

	private MultipartFile pictureFile;
	// The .{8,16} in the regex enforces both length and composition; @Size would be redundant.
	@NotBlank
	@Pattern(regexp = "(?=.*[A-Z])(?=.*[!@#$&*_])(?=.*\\d)(?=.*[a-z]).{8,16}",
			message = "password must be 8-16 characters and include uppercase, lowercase, digit and special character")
	private String password;

	private boolean isSameShipping;
	@NotBlank
	private String govtIdType;
	@NotBlank
	private String govtId;
	private MultipartFile govtIdPic;

	public CustomerDTO() {super();}
	
	public CustomerDTO(String username, String name, Date dob,
			long contact, String gender, String regId,
			String email) {
		super(username, name, dob, contact, gender, regId, email);
	}

	/**
	 * @return the pictureFile
	 */
	public MultipartFile getPictureFile() {
		return pictureFile;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the isSameShipping
	 */
	public boolean isSameShipping() {
		return isSameShipping;
	}

	/**
	 * @return the govtIdType
	 */
	public String getGovtIdType() {
		return govtIdType;
	}

	/**
	 * @return the govtId
	 */
	public String getGovtId() {
		return govtId;
	}

	/**
	 * @return the govtIdPic
	 */
	public MultipartFile getGovtIdPic() {
		return govtIdPic;
	}

	/**
	 * @param pictureFile
	 *            the pictureFile to set
	 */
	public void setPictureFile(MultipartFile pictureFile) {
		this.pictureFile = pictureFile;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param isSameShipping
	 *            the isSameShipping to set
	 */
	public void setSameShipping(boolean isSameShipping) {
		this.isSameShipping = isSameShipping;
	}

	/**
	 * @param govtIdType
	 *            the govtIdType to set
	 */
	public void setGovtIdType(String govtIdType) {
		this.govtIdType = govtIdType;
	}

	/**
	 * @param govtId
	 *            the govtId to set
	 */
	public void setGovtId(String govtId) {
		this.govtId = govtId;
	}

	/**
	 * @param govtIdPic
	 *            the govtIdPic to set
	 */
	public void setGovtIdPic(MultipartFile govtIdPic) {
		this.govtIdPic = govtIdPic;
	}

}
