package com.niit.lookatme.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
	
	private AddressMeta addressMeta;
	
	@Column(name ="POSTAL_CODE", nullable=false)
	private int postalCode;

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
