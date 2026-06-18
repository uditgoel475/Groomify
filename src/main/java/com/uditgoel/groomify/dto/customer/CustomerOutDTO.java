package com.uditgoel.groomify.dto.customer;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uditgoel.groomify.dto.AddressInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class CustomerOutDTO {

	@NotBlank
	@Size(max = 64)
	private String username;
	@NotBlank
	@Size(max = 128)
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotNull
	@Past
	private Date dob;
	@Min(value = 1000000000L, message = "contact must be a 10-digit number")
	@Max(value = 9999999999L, message = "contact must be a 10-digit number")
	private long contact;
	// alternateContact is optional (0 = unset). When set, must also be 10-digit; primitive long
	// can't carry @NotNull, so we accept 0 as "unset" and validate only the upper bound.
	@Max(value = 9999999999L, message = "alternateContact must be a 10-digit number when set")
	private long alternateContact;
	@NotBlank
	private String gender;
	@Valid
	private AddressInput billingAddress;
	@Valid
	private AddressInput shippingAddress;
	@NotBlank
	private String regId;
	@NotBlank
	@Email
	@Size(max = 40)
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
