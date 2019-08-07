package com.niit.lookatme.services.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;

import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.services.group.dao.GroupServicePackage;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "SERVICE")
public class Service extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7462113053434816178L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERVICE_ID", nullable = false, updatable = false)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;
	
	@Column(name = "PRICE", nullable = false)
	private Long price;
	
	@Column(name = "HSN/SAC")
	private String hsn;
	
	@Temporal(value = TemporalType.TIME)
	@Column(name = "SERVICE_TIME")
	private Date time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "SERVICE_TYPE_ID")
	private ServiceType serviceGroup;
	
	@Column(name = "IS_ACTIVE")
	@ColumnDefault("true")
	private Boolean isActive;
	
	@OneToMany(mappedBy = "SERVICE")
	private List<GroupServicePackage> groupServicePackages;

	public String getHsn() {
		return hsn;
	}

	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public ServiceType getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(ServiceType serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	public Long getId() {
		return id;
	}

	/**
	 * @return the isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the groupServicePackages
	 */
	public List<GroupServicePackage> getGroupServicePackages() {
		return groupServicePackages;
	}

	/**
	 * @param groupServicePackages the groupServicePackages to set
	 */
	public void setGroupServicePackages(List<GroupServicePackage> groupServicePackages) {
		this.groupServicePackages = groupServicePackages;
	}

	
	
}
