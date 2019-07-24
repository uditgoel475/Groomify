package com.niit.lookatme.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "SKILLS")
public class Skills {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SKILL_ID", nullable = false, updatable = false)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "IS_SKILL_ACTIVE", unique = true)
	@ColumnDefault("true")
	private Boolean isSkillActive;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the isSkillActive
	 */
	public Boolean getIsSkillActive() {
		return isSkillActive;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param isSkillActive
	 *            the isSkillActive to set
	 */
	public void setIsSkillActive(Boolean isSkillActive) {
		this.isSkillActive = isSkillActive;
	}
}
