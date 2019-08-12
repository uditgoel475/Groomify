package com.niit.lookatme.customer.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UpdateCustomerOrderInput {
	private String customerOrderRequestId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date appointmentDate;
	
	private Map<String, List<CustomerJobsInput>> initiateJobs; // jobid, [subjobids, employee, datetime]
	private Map<String, List<CustomerJobsInput>> endJobs; // jobid, [subjobids, employee, datetime]
	private Map<String, List<String>> cancelJobs; // jobid, [subjobids]
	/**
	 * @return the customerOrderRequestId
	 */
	public String getCustomerOrderRequestId() {
		return customerOrderRequestId;
	}
	/**
	 * @return the appointmentDate
	 */
	public Date getAppointmentDate() {
		return appointmentDate;
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
	 * @param customerOrderRequestId the customerOrderRequestId to set
	 */
	public void setCustomerOrderRequestId(String customerOrderRequestId) {
		this.customerOrderRequestId = customerOrderRequestId;
	}
	/**
	 * @param appointmentDate the appointmentDate to set
	 */
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	/**
	 * @param initiateJobs the initiateJobs to set
	 */
	public void setInitiateJobs(Map<String, List<CustomerJobsInput>> initiateJobs) {
		this.initiateJobs = initiateJobs;
	}
	/**
	 * @param endJobs the endJobs to set
	 */
	public void setEndJobs(Map<String, List<CustomerJobsInput>> endJobs) {
		this.endJobs = endJobs;
	}
	/**
	 * @return the cancelJobs
	 */
	public Map<String, List<String>> getCancelJobs() {
		return cancelJobs;
	}
	/**
	 * @param cancelJobs the cancelJobs to set
	 */
	public void setCancelJobs(Map<String, List<String>> cancelJobs) {
		this.cancelJobs = cancelJobs;
	}
}
