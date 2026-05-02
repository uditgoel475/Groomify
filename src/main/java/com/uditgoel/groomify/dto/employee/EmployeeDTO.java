package com.uditgoel.groomify.dto.employee;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.uditgoel.groomify.dao.Gender;
import com.uditgoel.groomify.dto.AddressInput;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class EmployeeDTO {

	@NotBlank
	@Size(max = 128)
	private String name;
	@NotBlank
	@Size(max = 64)
	private String username;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@NotNull
	@Past
	private Date dob;
	@Min(value = 1000000000L, message = "primaryContact must be a 10-digit number")
	@Max(value = 9999999999L, message = "primaryContact must be a 10-digit number")
	private long primaryContact;
	@NotNull
	private Gender gender;
	@Valid
	private AddressInput currentAddress;
	@Valid
	private AddressInput permanentAddress;
	private String regId;
	@NotBlank
	@Email
	@Size(max = 40)
	private String email;

	public EmployeeDTO() {
		super();
	}

	public EmployeeDTO(String name, String username, Date dob, long primaryContact, Gender gender,
			AddressInput currentAddress, AddressInput permanentAddress) {
		super();
		this.name = name;
		this.username = username;
		this.dob = dob;
		this.primaryContact = primaryContact;
		this.gender = gender;
		this.currentAddress = currentAddress;
		this.permanentAddress = permanentAddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @return the primaryContact
	 */
	public long getPrimaryContact() {
		return primaryContact;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the currentAddress
	 */
	public AddressInput getCurrentAddress() {
		return currentAddress;
	}

	/**
	 * @return the permanentAddress
	 */
	public AddressInput getPermanentAddress() {
		return permanentAddress;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @param primaryContact
	 *            the primaryContact to set
	 */
	public void setPrimaryContact(long primaryContact) {
		this.primaryContact = primaryContact;
	}

	/**
	 * @param gender
	 *            the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @param currentAddress
	 *            the currentAddress to set
	 */
	public void setCurrentAddress(AddressInput currentAddress) {
		this.currentAddress = currentAddress;
	}

	/**
	 * @param permanentAddress
	 *            the permanentAddress to set
	 */
	public void setPermanentAddress(AddressInput permanentAddress) {
		this.permanentAddress = permanentAddress;
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
