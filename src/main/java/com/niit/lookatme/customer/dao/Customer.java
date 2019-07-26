package com.niit.lookatme.customer.dao;

import java.util.Date;

import javax.persistence.CascadeType;
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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.ColumnDefault;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.Password;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1913943908812645852L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "USERNAME", nullable = false)
	private String username;

	@Column(name = "PICTURE_URL", unique = true)
	private String pictureUrl;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PASSWORD", updatable = false, referencedColumnName = "PASSWORD_ID")
	private Password password;

	@Column(name = "EMAIL", unique = true)
	private String email;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "DOB")
	private Date dob;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "GENDER")
	private Gender gender;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "BILLING_ADDRESS")
	private Address billingAddress;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "SHIPPING_ADDRESS")
	private Address shippingAddress;

	@Column(name = "CONTACT", nullable = false)
	@ColumnDefault("0000000000")
	private Long contact;

	@Column(name = "ALTERNATE_CONTACT")
	private Long alternateContact;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "EXPIRATION_DATE")
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
	public Long getContact() {
		return contact;
	}

	/**
	 * @return the alternateContact
	 */
	public Long getAlternateContact() {
		return alternateContact;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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
	 * @param pictureUrl
	 *            the pictureUrl to set
	 */
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(Password password) {
		this.password = password;
	}

	/**
	 * @return the dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * @param dob
	 *            the dob to set
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param gender
	 *            the gender to set
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
	 * @param billingAddress
	 *            the billingAddress to set
	 */
	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	/**
	 * @param shippingAddress
	 *            the shippingAddress to set
	 */
	public void setShippingAddress(Address shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public void setContact(Long contact) {
		this.contact = contact;
	}

	/**
	 * @param alternateContact
	 *            the alternateContact to set
	 */
	public void setAlternateContact(Long alternateContact) {
		this.alternateContact = alternateContact;
	}

	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(alternateContact).append(billingAddress).append(contact).append(dob)
				.append(email).append(expirationDate).append(gender).append(name).append(pictureUrl)
				.append(shippingAddress).append(username).toHashCode();
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Customer rhs = (Customer) obj;
		return new EqualsBuilder().append(alternateContact, rhs.alternateContact)
				.append(billingAddress, rhs.billingAddress).append(contact, rhs.contact).append(dob, rhs.dob)
				.append(email, rhs.email).append(expirationDate, rhs.expirationDate).append(gender, rhs.gender)
				.append(name, rhs.name).append(pictureUrl, rhs.pictureUrl).append(shippingAddress, rhs.shippingAddress)
				.append(username, rhs.username).isEquals();
	}

}
