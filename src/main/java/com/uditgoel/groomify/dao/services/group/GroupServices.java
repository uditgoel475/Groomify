package com.uditgoel.groomify.dao.services.group;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import com.uditgoel.groomify.dao.AuditInfo;

/**
 * Group of services to offer to customer. Contains the different package names, Eg: Bridal package, Groom's package
 */

@Entity
@Table(name = "GROUP_SERVICE")
public class GroupServices extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 960687438288196515L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUP_SERVICE_ID", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false)
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "MARKED_PRICE", nullable = false)
	private Double markedPrice;
	
	@Column(name = "FINAL_PRICE")
	private Double finalPrice;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", nullable = false)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", nullable = false)
	private Date endDate;
	
	@OneToMany(mappedBy = "groupService")
	private List<GroupServicePackage> groupServicePackages;

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
	 * @return the markedPrice
	 */
	public Double getMarkedPrice() {
		return markedPrice;
	}

	/**
	 * @return the finalPrice
	 */
	public Double getFinalPrice() {
		return finalPrice;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return the groupServicePackages
	 */
	public List<GroupServicePackage> getGroupServicePackages() {
		return groupServicePackages;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param markedPrice the markedPrice to set
	 */
	public void setMarkedPrice(Double markedPrice) {
		this.markedPrice = markedPrice;
	}

	/**
	 * @param finalPrice the finalPrice to set
	 */
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param groupServicePackages the groupServicePackages to set
	 */
	public void setGroupServicePackages(List<GroupServicePackage> groupServicePackages) {
		this.groupServicePackages = groupServicePackages;
	}
}
