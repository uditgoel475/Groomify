package com.uditgoel.groomify.dao.services.group;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import com.uditgoel.groomify.dao.services.Service;

/**
 * Group of services mapped to the combination of services to be offered to customer.
 */

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
	@MapsId("serviceId")
	@JoinColumn(name = "SERVICE_ID")
	private Service service;

	@ManyToOne
	@MapsId("groupServiceId")
	@JoinColumn(name = "GROUP_SERVICE_ID")
	private GroupServices groupService;

	@Column(name = "DESCRIPTION")
	private String description;

	protected GroupServicePackage() {
	}

	public GroupServicePackage(Service service, GroupServices groupService, String description) {
		this.service = service;
		this.groupService = groupService;
		this.description = description;
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
}
