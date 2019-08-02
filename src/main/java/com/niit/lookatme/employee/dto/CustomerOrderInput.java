package com.niit.lookatme.employee.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerOrderInput {

	private String customerOrderRequestId;
	private String username;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date appointmentDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date initTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date endTime;
	private Map<Date, List<String>> createNewServicesMap;
	private Map<String, List<CustomerJobsInput>> initiateJobs; // jobid, [subjobids, employee, datetime]
	private Map<String, List<CustomerJobsInput>> endJobs; // jobid, [subjobids, employee, datetime]

	public CustomerOrderInput(String customerOrderRequestId, String username, Date appointmentDate, Date initTime) {
		super();
		this.customerOrderRequestId = customerOrderRequestId;
		this.username = username;
		this.appointmentDate = appointmentDate;
		this.initTime = initTime;
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
	 * @return the initTime
	 */
	public Date getInitTime() {
		return initTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @return the createNewServicesMap
	 */
	public Map<Date, List<String>> getCreateNewServicesMap() {
		return createNewServicesMap;
	}

	/**
	 * @return the initiateJobs
	 */
	public Map<String, List<CustomerJobsInput>> getInitiateJobs() {
		return initiateJobs;
	}

	/**
	 * @return the endJobs
	 */
	public Map<String, List<CustomerJobsInput>> getEndJobs() {
		return endJobs;
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
	 * @param initTime
	 *            the initTime to set
	 */
	public void setInitTime(Date initTime) {
		this.initTime = initTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * @param createNewServicesMap
	 *            the createNewServicesMap to set
	 */
	public void setCreateNewServicesMap(Map<Date, List<String>> createNewServicesMap) {
		this.createNewServicesMap = createNewServicesMap;
	}

	/**
	 * @param initiateJobs
	 *            the initiateJobs to set
	 */
	public void setInitiateJobs(Map<String, List<CustomerJobsInput>> initiateJobs) {
		this.initiateJobs = initiateJobs;
	}

	/**
	 * @param endJobs
	 *            the endJobs to set
	 */
	public void setEndJobs(Map<String, List<CustomerJobsInput>> endJobs) {
		this.endJobs = endJobs;
	}

}
