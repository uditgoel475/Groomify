package com.uditgoel.groomify.dao;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "MEMBERSHIP_TYPE")
public class Membership extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7432016907687062806L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TYPE_ID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "MEMBERSHIP_TYPE", nullable = false)
	private String membershipType;
	
	@Column(name = "MEMBERSHIP_CODE")
	private String membershipCode;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "START_DATE")
	private Date startDate;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "LAST_DATE")
	private Date lastDate;
	
	@Column(name = "DURATION_DAYS", nullable = false)
	private int durationDays;
	
	@Column(name = "PRICE", nullable = false)
	private Double price;

	@Column(name = "DESCRIPTION")
	private String description;

	public Membership(String membershipType, String membershipCode, Date startDate, Date lastDate, int durationDays,
			Double price) {
		super();
		this.membershipType = membershipType;
		this.membershipCode = membershipCode;
		this.startDate = startDate;
		this.lastDate = lastDate;
		this.durationDays = durationDays;
		this.price = price;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the membershipType
	 */
	public String getMembershipType() {
		return membershipType;
	}

	/**
	 * @return the membershipCode
	 */
	public String getMembershipCode() {
		return membershipCode;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the lastDate
	 */
	public Date getLastDate() {
		return lastDate;
	}

	/**
	 * @return the durationDays
	 */
	public int getDurationDays() {
		return durationDays;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param membershipType the membershipType to set
	 */
	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}

	/**
	 * @param membershipCode the membershipCode to set
	 */
	public void setMembershipCode(String membershipCode) {
		this.membershipCode = membershipCode;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param lastDate the lastDate to set
	 */
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	/**
	 * @param durationDays the durationDays to set
	 */
	public void setDurationDays(int durationDays) {
		this.durationDays = durationDays;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
