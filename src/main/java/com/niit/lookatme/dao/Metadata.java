package com.niit.lookatme.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "METADATA")
public class Metadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "METADATA_ID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false, unique = true)
	private String name;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@CreatedDate
	@CreationTimestamp
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "META_1")
	private String meta1;
	
	@Column(name = "META_2")
	private String meta2;
	
	@Column(name = "META_3")
	private String meta3;
	
	@Column(name = "META_4")
	private String meta4;
	
	@Column(name = "META_5")
	private String meta5;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the meta1
	 */
	public String getMeta1() {
		return meta1;
	}

	/**
	 * @return the meta2
	 */
	public String getMeta2() {
		return meta2;
	}

	/**
	 * @return the meta3
	 */
	public String getMeta3() {
		return meta3;
	}

	/**
	 * @return the meta4
	 */
	public String getMeta4() {
		return meta4;
	}

	/**
	 * @return the meta5
	 */
	public String getMeta5() {
		return meta5;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param meta1 the meta1 to set
	 */
	public void setMeta1(String meta1) {
		this.meta1 = meta1;
	}

	/**
	 * @param meta2 the meta2 to set
	 */
	public void setMeta2(String meta2) {
		this.meta2 = meta2;
	}

	/**
	 * @param meta3 the meta3 to set
	 */
	public void setMeta3(String meta3) {
		this.meta3 = meta3;
	}

	/**
	 * @param meta4 the meta4 to set
	 */
	public void setMeta4(String meta4) {
		this.meta4 = meta4;
	}

	/**
	 * @param meta5 the meta5 to set
	 */
	public void setMeta5(String meta5) {
		this.meta5 = meta5;
	}
}
