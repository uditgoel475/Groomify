package com.niit.lookatme.customer.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CreateCustomerOrderInput {

	private String customerOrderRequestId;
	private String username;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date appointmentDate;
	
	private Map<Date, List<String>> createNewServicesMap;

	public CreateCustomerOrderInput(String customerOrderRequestId, String username, Date appointmentDate) {
		super();
		this.customerOrderRequestId = customerOrderRequestId;
		this.username = username;
		this.appointmentDate = appointmentDate;
	}

	/**
	 * @return the customerOrderRequestId
	 */
	public String getCustomerOrderRequestId() {
		return customerOrderRequestId;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the appointmentDate
	 */
	public Date getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * @return the createNewServicesMap
	 */
	public Map<Date, List<String>> getCreateNewServicesMap() {
		return createNewServicesMap;
	}

	/**
	 * @param customerOrderRequestId
	 *            the customerOrderRequestId to set
	 */
	public void setCustomerOrderRequestId(String customerOrderRequestId) {
		this.customerOrderRequestId = customerOrderRequestId;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param appointmentDate
	 *            the appointmentDate to set
	 */
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	/**
	 * @param createNewServicesMap
	 *            the createNewServicesMap to set
	 */
	public void setCreateNewServicesMap(Map<Date, List<String>> createNewServicesMap) {
		this.createNewServicesMap = createNewServicesMap;
	}
}