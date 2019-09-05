package com.niit.lookatme.dto;

import java.time.LocalTime;

public class ServiceExtDTO extends ServiceDTO{

	private Double price;
	private LocalTime serviceTime;
	private Boolean active;
	
	public ServiceExtDTO() {
		super();
	}
	
	public ServiceExtDTO(String serviceType, String hsn, String name, Double price, LocalTime serviceTime,
			Boolean active) {
		super(serviceType, hsn, name);
		this.price = price;
		this.serviceTime = serviceTime;
		this.active = active;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @return the serviceTime
	 */
	public LocalTime getServiceTime() {
		return serviceTime;
	}

	/**
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @param serviceTime
	 *            the serviceTime to set
	 */
	public void setServiceTime(LocalTime serviceTime) {
		this.serviceTime = serviceTime;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
}
