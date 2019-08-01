package com.niit.lookatme.employee.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerOrderInput {

	private String customerOrderRequestId;
	private String username;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date appointmentDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date appointmentTime;
	private List<String> serviceList;

	public CustomerOrderInput(String customerOrderRequestId, String username, Date appointmentDate, Date appointmentTime, List<String> serviceList) {
		super();
		this.customerOrderRequestId = customerOrderRequestId;
		this.username = username;
		this.appointmentDate = appointmentDate;
		this.appointmentTime = appointmentTime;
		this.serviceList = serviceList;
	}

	/**
	 * @return the customerOrderRequestId
	 */
	public String getCustomerOrderRequestId() {
		return customerOrderRequestId;
	}

	/**
	 * @param customerOrderRequestId the customerOrderRequestId to set
	 */
	public void setCustomerOrderRequestId(String customerOrderRequestId) {
		this.customerOrderRequestId = customerOrderRequestId;
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
	 * @return the appointmentTime
	 */
	public Date getAppointmentTime() {
		return appointmentTime;
	}

	/**
	 * @return the serviceList
	 */
	public List<String> getServiceList() {
		return serviceList;
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
	 * @param appointmentTime
	 *            the appointmentTime to set
	 */
	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	/**
	 * @param serviceList
	 *            the serviceList to set
	 */
	public void setServiceList(List<String> serviceList) {
		this.serviceList = serviceList;
	}

}
