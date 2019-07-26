package com.niit.lookatme.inventory.dao;

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
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.PaymentModes;
import com.niit.lookatme.dao.Rating;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "VENDOR")
public class Vendor extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6539048894121395352L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_ID", nullable = false, updatable = false)
    private Long id;
	
	@Column(name = "COMPANY_NAME", nullable = false)
	private String companyName;
	
	@Column(name = "VENDOR_REG_ID", nullable = false, unique=true)
	private String vendorRegId;
	
	@Column(name = "CONTACT_FIRST_NAME", nullable = false)
	private String contactPersonFirstName;
	
	@Column(name = "CONTACT_LAST_NAME")
	private String contactPersonLastName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ADDRESS",referencedColumnName= "ADDRESS_ID")
	private Address address;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "GENDER")
	@ColumnDefault("'MALE'")
	private Gender contactPersonGender;
	
	@Column(name = "SITE_URL")
	private String siteURL;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "PAYMENT_METHOD")
	@ColumnDefault("'ACCOUNT_TRANSFER'")
	private PaymentModes paymentMethod;
	
	@Column(name = "LOGO_URL")
	private String logoUrl;
	
	@Column(name = "IS_TRUSTED_VENDOR", nullable = false)
	@ColumnDefault("true")
	private boolean isTrustedVendor;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "RATING", nullable = false)
	@ColumnDefault("'AVERAGE'")
	private Rating rating;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @return the vendorRegId
	 */
	public String getVendorRegId() {
		return vendorRegId;
	}

	/**
	 * @return the contactPersonFirstName
	 */
	public String getContactPersonFirstName() {
		return contactPersonFirstName;
	}

	/**
	 * @return the contactPersonLastName
	 */
	public String getContactPersonLastName() {
		return contactPersonLastName;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @return the contactPersonGender
	 */
	public Gender getContactPersonGender() {
		return contactPersonGender;
	}

	/**
	 * @return the siteURL
	 */
	public String getSiteURL() {
		return siteURL;
	}

	/**
	 * @return the paymentMethod
	 */
	public PaymentModes getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @return the logoUrl
	 */
	public String getLogoUrl() {
		return logoUrl;
	}

	/**
	 * @return the isTrustedVendor
	 */
	public boolean isTrustedVendor() {
		return isTrustedVendor;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @param vendorRegId the vendorRegId to set
	 */
	public void setVendorRegId(String vendorRegId) {
		this.vendorRegId = vendorRegId;
	}

	/**
	 * @param contactPersonFirstName the contactPersonFirstName to set
	 */
	public void setContactPersonFirstName(String contactPersonFirstName) {
		this.contactPersonFirstName = contactPersonFirstName;
	}

	/**
	 * @param contactPersonLastName the contactPersonLastName to set
	 */
	public void setContactPersonLastName(String contactPersonLastName) {
		this.contactPersonLastName = contactPersonLastName;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @param contactPersonGender the contactPersonGender to set
	 */
	public void setContactPersonGender(Gender contactPersonGender) {
		this.contactPersonGender = contactPersonGender;
	}

	/**
	 * @param siteURL the siteURL to set
	 */
	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(PaymentModes paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @param logoUrl the logoUrl to set
	 */
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	/**
	 * @param isTrustedVendor the isTrustedVendor to set
	 */
	public void setTrustedVendor(boolean isTrustedVendor) {
		this.isTrustedVendor = isTrustedVendor;
	}
	
}
