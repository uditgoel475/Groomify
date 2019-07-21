package com.niit.lookatme.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CUSTOMERS")
public class Customers extends AuditInfo{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1913943908812645852L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CUSTOMER_ID", updatable = false, nullable = false)
    private Long id;
	
	@Column(name = "NAME", nullable = false, columnDefinition = "Customer Name")
	private String name;

	@Column(name = "USERNAME", nullable = false, columnDefinition = "Customer User Id")
	private String username;

	@Column(name = "PICTURE_URL", columnDefinition = "Customer Passport Picture")
	private String pictureUrl;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSWORD", updatable = false, referencedColumnName = "PASSWORD_ID", columnDefinition = "Customer Login Password Definition")
	private Password password;
	
	@Column(name = "EMAIL", columnDefinition = "Customer Email")
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "GENDER", columnDefinition = "Customer Gender")
	private Gender gender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Column(name = "ADDRESS", columnDefinition = "Customer Address")
	private Address address;
	
	@Column(name = "CONTACT", nullable = false, columnDefinition = "Customer's Contact")
	private long contact;
	
	@Column(name = "ALTERNATE_CONTACT", columnDefinition = "Customer's Alternate Contact")
	private long alternateContact;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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
	 * @return the pictureUrl
	 */
	public String getPictureUrl() {
		return pictureUrl;
	}

	/**
	 * @return the password
	 */
	public Password getPassword() {
		return password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param pictureUrl the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(Password password) {
		this.password = password;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(long contact) {
		this.contact = contact;
	}

	/**
	 * @param alternateContact the alternateContact to set
	 */
	public void setAlternateContact(long alternateContact) {
		this.alternateContact = alternateContact;
	}

	
}
