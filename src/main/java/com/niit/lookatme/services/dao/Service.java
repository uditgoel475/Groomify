package com.niit.lookatme.services.dao;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.niit.lookatme.dao.AuditInfo;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "Service")
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
	private Time time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE_GROUP", referencedColumnName = "SERVICE_GROUP_ID")
	private ServiceGroup serviceGroup;

	public String getHsn() {
		return hsn;
	}

	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
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

	public ServiceGroup getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(ServiceGroup serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	public Long getId() {
		return id;
	}

	
}
