package com.niit.lookatme.dao.employee;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
	
	@Enumerated(EnumType.STRING)
	@Column(name = "QUALIFICATION_TYPE")
	private Qualifications qualificationType;

	public EmployeeQualification() {
		super();
	}

	public EmployeeQualification(Qualifications qualificationType) {
		super();
		this.qualificationType = qualificationType;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the qualificationType
	 */
	public Qualifications getQualificationType() {
		return qualificationType;
	}

	/**
	 * @param qualificationType the qualificationType to set
	 */
	public void setQualificationType(Qualifications qualificationType) {
		this.qualificationType = qualificationType;
	}
	
	
}
