package com.uditgoel.groomify.dto;

public class ServiceDTO {

	private String serviceType;
	private String hsn;
	private String name;

	public ServiceDTO(String serviceType, String hsn, String name) {
		super();
		this.serviceType = serviceType;
		this.hsn = hsn;
		this.name = name;
	}

	public ServiceDTO() {
		super();
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @return the hsn
	 */
	public String getHsn() {
		return hsn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @param hsn
	 *            the hsn to set
	 */
	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
