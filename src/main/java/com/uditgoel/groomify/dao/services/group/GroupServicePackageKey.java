package com.uditgoel.groomify.dao.services.group;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

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

	public GroupServicePackageKey() {
	}

	public GroupServicePackageKey(Long serviceId, Long groupServiceId) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupServiceId == null) ? 0 : groupServiceId.hashCode());
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroupServicePackageKey other = (GroupServicePackageKey) obj;
		if (groupServiceId == null) {
			if (other.groupServiceId != null)
				return false;
		} else if (!groupServiceId.equals(other.groupServiceId))
			return false;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}
}
