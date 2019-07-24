package com.niit.lookatme.dao;

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
@Table(name = "QUALIFICATION")
public class EmployeeQualification implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3232401916232956454L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "QUALIFICATION_ID")
	private Long id;
	
	@Column(name = "QUALIFICATION_TYPE")
	private String qualificationType;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the qualificationType
	 */
	public String getQualificationType() {
		return qualificationType;
	}

	/**
	 * @param qualificationType the qualificationType to set
	 */
	public void setQualificationType(String qualificationType) {
		this.qualificationType = qualificationType;
	}
	
	
}
