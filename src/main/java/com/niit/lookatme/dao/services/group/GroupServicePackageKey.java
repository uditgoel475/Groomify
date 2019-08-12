package com.niit.lookatme.dao.services.group;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GroupServicePackageKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6815535271220674219L;

	@Column(name = "SERVICE_ID")
	private Long serviceId;

	@Column(name = "GROUP_SERVICE_ID")
	private Long groupServiceId;

	public GroupServicePackageKey(Long serviceId, Long groupServiceId) {
		super();
		this.serviceId = serviceId;
		this.groupServiceId = groupServiceId;
	}

	/**
	 * @return the serviceId
	 */
	public Long getServiceId() {
		return serviceId;
	}

	/**
	 * @return the groupServiceId
	 */
	public Long getGroupServiceId() {
		return groupServiceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @param groupServiceId the groupServiceId to set
	 */
	public void setGroupServiceId(Long groupServiceId) {
		this.groupServiceId = groupServiceId;
	}
}
