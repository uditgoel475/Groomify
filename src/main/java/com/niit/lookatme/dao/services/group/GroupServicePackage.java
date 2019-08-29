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
	private Service service;

	@ManyToOne
	@MapsId("GROUP_SERVICE_ID")
	@JoinColumn(name = "GROUP_SERVICE_ID")
	private GroupServices groupService;

	@Column(name = "NOTE")
	private String serviceNote;

	public GroupServicePackage(Service service, GroupServices groupService, String serviceNote) {
		super();
		this.service = service;
		this.groupService = groupService;
		this.serviceNote = serviceNote;
	}

	/**
	 * @return the id
	 */
	public GroupServicePackageKey getId() {
		return id;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
	}

	/**
	 * @return the groupService
	 */
	public GroupServices getGroupService() {
		return groupService;
	}

	/**
	 * @return the serviceNote
	 */
	public String getServiceNote() {
		return serviceNote;
	}

	/**
	 * @param serviceId the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @param groupServiceId the groupService to set
	 */
	public void setGroupService(GroupServices groupService) {
		this.groupService = groupService;
	}

	/**
	 * @param serviceNote the serviceNote to set
	 */
	public void setServiceNote(String serviceNote) {
		this.serviceNote = serviceNote;
	}
}
