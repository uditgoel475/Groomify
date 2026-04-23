package com.uditgoel.groomify.dao.services;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.ColumnDefault;

import com.uditgoel.groomify.dao.AuditInfo;
import com.uditgoel.groomify.dao.services.group.GroupServicePackage;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "SERVICE", uniqueConstraints = { @UniqueConstraint(columnNames = { "NAME", "HSN/SAC", "SERVICE_TYPE" }) })
public class Service extends AuditInfo {

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

	@Column(name = "PRICE")
	private Double price;

	@Column(name = "HSN/SAC")
	private String hsn;

	@Temporal(value = TemporalType.TIME)
	@Column(name = "SERVICE_TIME")
	private Date time;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "SERVICE_TYPE", referencedColumnName = "SERVICE_TYPE_ID")
	private ServiceType serviceGroup;

	@Column(name = "IS_ACTIVE")
	@ColumnDefault("true")
	private Boolean isActive;

	@OneToMany(mappedBy = "service")
	private List<GroupServicePackage> groupServicePackages;
	
	public Service() {
		super();
	}

	public Service(String name, Double price, String hsn, Date time, ServiceType serviceGroup, Boolean isActive) {
		super();
		this.name = name;
		this.price = price;
		this.hsn = hsn;
		this.time = time;
		this.serviceGroup = serviceGroup;
		this.isActive = isActive;
	}

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
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
	 * @param isActive
	 *            the isActive to set
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
	 * @param groupServicePackages
	 *            the groupServicePackages to set
	 */
	public void setGroupServicePackages(List<GroupServicePackage> groupServicePackages) {
		this.groupServicePackages = groupServicePackages;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(name)
				.append(price).append(hsn).append(time)
				.append(serviceGroup).append(isActive)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Service rhs = (Service) obj;
		return new EqualsBuilder().append(name, rhs.name)
				.append(price, rhs.price).append(hsn, rhs.hsn).append(time, rhs.time)
				.append(serviceGroup, rhs.serviceGroup).append(isActive, rhs.isActive)
				.isEquals();
	}

}
