package com.niit.lookatme.dao;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "USERNAME", nullable = false)
	private String username;

	@Column(name = "PICTURE_URL", unique=true)
	private String pictureUrl;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSWORD", updatable = false, referencedColumnName = "PASSWORD_ID")
	private Password password;
	
	@Column(name = "EMAIL", unique=true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "GENDER")
	private Gender gender;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BILLING_ADDRESS")
	private Address billingAddress;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIPPING_ADDRESS")
	private Address shippingAddress;
	
	@Column(name = "CONTACT", nullable = false, columnDefinition = "integer default 0000000000")
	private long contact;
	
	@Column(name = "ALTERNATE_CONTACT")
	private long alternateContact;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name ="EXPIRATION_DATE")
	private Date expirationDate;

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

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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
	 * @return the billingAddress
	 */
	public Address getBillingAddress() {
		return billingAddress;
	}

	/**
	 * @return the shippingAddress
	 */
	public Address getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * @param billingAddress the billingAddress to set
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @param shippingAddress the shippingAddress to set
	 */
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
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
