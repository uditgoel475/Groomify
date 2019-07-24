package com.niit.lookatme.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

/**
 * 
 * @author ugoel1
 *
 */
@Entity
@Table(name ="ADDRESS_META")
public class AddressMeta implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1940700984417874587L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ADDRESS_META_ID", nullable = false, updatable = false)
    private Long id;
	
	@Column(name ="COUNTRY", nullable=false)
	@ColumnDefault("'India'")
	private String country;
	
	@Column(name ="STATE", nullable=false)
	private String state;
	
	@Column(name ="CITY", nullable=false)
	private String city;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

}
