package com.niit.lookatme.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SkillSet")
public class SkillSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SKILLSET_ID", nullable=false, updatable = false)
	private Long id;
	
	private Skills skills;
	
	private SalonMembers salonMembers;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the skills
	 */
	public Skills getSkills() {
		return skills;
	}

	/**
	 * @return the salonMembers
	 */
	public SalonMembers getSalonMembers() {
		return salonMembers;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(Skills skills) {
		this.skills = skills;
	}

	/**
	 * @param salonMembers the salonMembers to set
	 */
	public void setSalonMembers(SalonMembers salonMembers) {
		this.salonMembers = salonMembers;
	}
  
}
