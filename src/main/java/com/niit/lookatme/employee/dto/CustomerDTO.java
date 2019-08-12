package com.niit.lookatme.employee.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.niit.lookatme.dto.AddressInput;

public class CustomerDTO {

	private String username;
	private String fName;
	private String mName;
	private String lName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dob;
	private MultipartFile pictureFile;
	private String password;

	private long contact;
	private long alternateContact;
	private String gender;
	private AddressInput billingAddress;
	private AddressInput shippingAddress;
	private boolean isSameShipping;
	private String regId;
	private String email;
	private String govtIdType;
	private String govtId;
	private MultipartFile govtIdPic;

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
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
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
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
	 * @return the contact
	 */
	public long getContact() {
		return contact;
	}

	/**
	 * @return the alternateContact
	 */
	public long getAlternateContact() {
		return alternateContact;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the billingAddress
	 */
	public AddressInput getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @return the shippingAddress
	 */
	public AddressInput getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @return the isSameShipping
	 */
	public boolean isSameShipping() {
		return isSameShipping;
	}

	/**
	 * @return the regId
	 */
	public String getRegId() {
		return regId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
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
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
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
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(long contact) {
		this.contact = contact;
	}

	/**
	 * @param alternateContact
	 *            the alternateContact to set
	 */
	public void setAlternateContact(long alternateContact) {
		this.alternateContact = alternateContact;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(AddressInput billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @param shippingAddress
	 *            the shippingAddress to set
	 */
	public void setShippingAddress(AddressInput shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @param isSameShipping
	 *            the isSameShipping to set
	 */
	public void setSameShipping(boolean isSameShipping) {
		this.isSameShipping = isSameShipping;
	}

	/**
	 * @param regId
	 *            the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
