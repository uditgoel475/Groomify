package com.uditgoel.groomify.dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name="ADDRESS") 
public class Address extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4956135406280067725L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ADDRESS_ID", nullable = false, updatable = false)
    private Long id;
	
	@Column(name ="ADDRESS_1", nullable=false)
	private String address1;
	
	@Column(name ="ADDRESS_2")
	private String address2;
	
	@Column(name ="ADDRESS_3")
	private String address3;
	
	@Column(name ="STATE", nullable=false)
	private String state;		
	
	@Column(name ="CITY", nullable=false)
	private String city;
	
	@Column(name ="REGION")
	private String region;
	
	@Column(name ="POSTAL_CODE", nullable=false)
	private int postalCode;
	
	@Column(name ="COUNTRY", nullable=false)
	private String country;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the address1
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @return the address3
	 */
	public String getAddress3() {
		return address3;
	}

	

	/**
	 * @return the postalCode
	 */
	public int getPostalCode() {
		return postalCode;
	}

	/**
	 * @param address1 the address1 to set
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @param address3 the address3 to set
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}
}
