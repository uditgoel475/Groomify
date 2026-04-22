package com.uditgoel.groomify.dao;

import java.io.Serializable;

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
@Table(name = "GOVT_ID_TYPE")
public class GovtIdType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3892424895642409524L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TYPE_ID")
	private Long id;

	@Column(name = "TYPE_NAME", nullable = false)
	private String typeName;
	
	@Column(name = "REGEX")
	private String regex;

	public GovtIdType(String typeName, String regex) {
		super();
		this.typeName = typeName;
		this.regex = regex;
	}
	
	public GovtIdType() {
		super();
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @return the regex
	 */
	public String getRegex() {
		return regex;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/**
	 * @param regex the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
}
