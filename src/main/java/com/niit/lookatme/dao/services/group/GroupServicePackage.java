package com.niit.lookatme.dao.services.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.niit.lookatme.dao.services.Service;

@Entity
@Table(name = "GROUP_SERVICE_PACKAGE")
public class GroupServicePackage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7943659605854044743L;

	@EmbeddedId
	private GroupServicePackageKey id;

	@ManyToOne
	@MapsId("SERVICE_ID")
	@JoinColumn(name = "SERVICE_ID")
	private Service serviceId;

	@ManyToOne
	@MapsId("GROUP_SERVICE_ID")
	@JoinColumn(name = "GROUP_SERVICE_ID")
	private GroupServices groupServiceId;

	@Column(name = "NOTE")
	private String serviceNote;

	public GroupServicePackage(Service serviceId, GroupServices groupServiceId, String serviceNote) {
		super();
		this.serviceId = serviceId;
		this.groupServiceId = groupServiceId;
		this.serviceNote = serviceNote;
	}

	/**
	 * @return the id
	 */
	public GroupServicePackageKey getId() {
		return id;
	}

	/**
	 * @return the serviceId
	 */
	public Service getServiceId() {
		return serviceId;
	}

	/**
	 * @return the groupServiceId
	 */
	public GroupServices getGroupServiceId() {
		return groupServiceId;
	}

	/**
	 * @return the serviceNote
	 */
	public String getServiceNote() {
		return serviceNote;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Service serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @param groupServiceId the groupServiceId to set
	 */
	public void setGroupServiceId(GroupServices groupServiceId) {
		this.groupServiceId = groupServiceId;
	}

	/**
	 * @param serviceNote the serviceNote to set
	 */
	public void setServiceNote(String serviceNote) {
		this.serviceNote = serviceNote;
	}
}
