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
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ColumnDefault;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
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

	@Column(name = "FIRST_NAME", nullable = false)
	private String fname;

	@Column(name = "MIDDLE_NAME")
	private String mname;

	@Column(name = "LAST_NAME")
	private String lname;

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
	@JoinColumn(name = "BILLING_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address billingAddress;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "SHIPPING_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address shippingAddress;
	
	@Column(name = "REGISTRATION_ID", nullable = false, unique = true)
	private String regId;

	@Column(name = "CONTACT", nullable = false)
	@ColumnDefault("0000000000")
	private Long contact;

	@Column(name = "ALTERNATE_CONTACT")
	private Long alternateContact;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GOVT_ID_TYPE", referencedColumnName = "TYPE_ID")
	private GovtIdType govtIdType;

	@Column(name = "GOVT_ID", nullable = false, unique = true)
	private String govtId;

	@Column(name = "GOVT_ID_SNAP_URL", unique = true)
	private String govtIdSnapUrl;

	@Temporal(value = TemporalType.DATE)
	@Column(name = "ACCOUNT_CLOSURE_DATE")
	private Date accountClosureDate;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
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

	public Date getAccountClosureDate() {
		return accountClosureDate;
	}

	public void setAccountClosureDate(Date accountClosureDate) {
		this.accountClosureDate = accountClosureDate;
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

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	/**
	 * @return the govtIdType
	 */
	public GovtIdType getGovtIdType() {
		return govtIdType;
	}

	/**
	 * @return the govtId
	 */
	public String getGovtId() {
		return govtId;
	}

	/**
	 * @return the govtIdSnapUrl
	 */
	public String getGovtIdSnapUrl() {
		return govtIdSnapUrl;
	}

	/**
	 * @param govtIdType the govtIdType to set
	 */
	public void setGovtIdType(GovtIdType govtIdType) {
		this.govtIdType = govtIdType;
	}

	/**
	 * @param govtId the govtId to set
	 */
	public void setGovtId(String govtId) {
		this.govtId = govtId;
	}

	/**
	 * @param govtIdSnapUrl the govtIdSnapUrl to set
	 */
	public void setGovtIdSnapUrl(String govtIdSnapUrl) {
		this.govtIdSnapUrl = govtIdSnapUrl;
	}

	/**
	 * @return the regId
	 */
	public String getRegId() {
		return regId;
	}

	/**
	 * @param regId the regId to set
	 */
	public void setRegId(String regId) {
		this.regId = regId;
	}
	
	@Transient
	public String getName() {
		return new StringBuilder(fname).append(" ").append(mname)
				.append(StringUtils.isEmpty(mname) ? StringUtils.EMPTY : " ").append(lname).toString().trim();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(alternateContact).append(billingAddress).append(contact).append(dob)
				.append(email).append(accountClosureDate).append(gender).append(fname).append(mname).append(lname)
				.append(pictureUrl).append(shippingAddress).append(username).append(govtIdType).append(govtId).append(regId).toHashCode();
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
				.append(email, rhs.email).append(accountClosureDate, rhs.accountClosureDate).append(gender, rhs.gender)
				.append(fname, rhs.fname).append(lname, rhs.lname).append(mname, rhs.mname)
				.append(pictureUrl, rhs.pictureUrl).append(shippingAddress, rhs.shippingAddress)
				.append(username, rhs.username).append(govtIdType, rhs.govtIdType).append(govtId, rhs.govtId).append(regId, rhs.regId).isEquals();
	}
	
}
