package com.niit.lookatme.dto.customer;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class CustomerDTO extends CustomerOutDTO {

	private String fName;
	private String mName;
	private String lName;
	private MultipartFile pictureFile;
	private String password;

	private boolean isSameShipping;
	private String govtIdType;
	private String govtId;
	private MultipartFile govtIdPic;

	public CustomerDTO() {super();}
	
	public CustomerDTO(String username, String name, Date dob,
			long contact, String gender, String regId,
			String email) {
		super(username, name, dob, contact, gender, regId, email);
	}

	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}

	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
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
	 * @param fName
	 *            the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * @param mName
	 *            the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}

	/**
	 * @param lName
	 *            the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
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
