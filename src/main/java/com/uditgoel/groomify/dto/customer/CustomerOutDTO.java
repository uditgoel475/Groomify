package com.uditgoel.groomify.dto.customer;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uditgoel.groomify.dto.AddressInput;

public class CustomerOutDTO {

	private String username;
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dob;
	private long contact;
	private long alternateContact;
	private String gender;
	private AddressInput billingAddress;
	private AddressInput shippingAddress;
	private String regId;
	private String email;

	public CustomerOutDTO() {super();}
	
	public CustomerOutDTO(String username, String name, Date dob,
			long contact, String gender, String regId,
			String email) {
		super();
		this.username = username;
		this.name = name;
		this.dob = dob;
		this.contact = contact;
		this.gender = gender;
		this.regId = regId;
		this.email = email;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
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
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
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
}
