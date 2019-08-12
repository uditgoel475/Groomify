package com.niit.lookatme.dto.customer;

import java.util.Date;

import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.dto.employee.EmployeeDTO;

public class CustomerJobCardDetailsOut {

	private String subJobCardId;
	private JobStatus subJobStatus;
	private EmployeeDTO employeeDTO;
	private String serviceName;
	private String customerFeedback;
	private Date jobStartTime;
	private Date jobEndTime;

	public CustomerJobCardDetailsOut() {super();}
	
	public CustomerJobCardDetailsOut(String subJobCardId, JobStatus subJobStatus,
			EmployeeDTO employeeDTO, String serviceName,
			String customerFeedback, Date jobStartTime, Date jobEndTime) {
		super();
		this.subJobCardId = subJobCardId;
		this.subJobStatus = subJobStatus;
		this.employeeDTO = employeeDTO;
		this.serviceName = serviceName;
		this.customerFeedback = customerFeedback;
		this.jobStartTime = jobStartTime;
		this.jobEndTime = jobEndTime;
	}

	/**
	 * @return the subJobCardId
	 */
	public String getSubJobCardId() {
		return subJobCardId;
	}

	/**
	 * @return the subJobStatus
	 */
	public JobStatus getSubJobStatus() {
		return subJobStatus;
	}

	/**
	 * @return the employeeDTO
	 */
	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the customerFeedback
	 */
	public String getCustomerFeedback() {
		return customerFeedback;
	}

	/**
	 * @return the jobStartTime
	 */
	public Date getJobStartTime() {
		return jobStartTime;
	}

	/**
	 * @return the jobEndTime
	 */
	public Date getJobEndTime() {
		return jobEndTime;
	}

	/**
	 * @param subJobCardId
	 *            the subJobCardId to set
	 */
	public void setSubJobCardId(String subJobCardId) {
		this.subJobCardId = subJobCardId;
	}

	/**
	 * @param subJobStatus
	 *            the subJobStatus to set
	 */
	public void setSubJobStatus(JobStatus subJobStatus) {
		this.subJobStatus = subJobStatus;
	}

	/**
	 * @param employeeDTO
	 *            the employeeDTO to set
	 */
	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	/**
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @param customerFeedback
	 *            the customerFeedback to set
	 */
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}

	/**
	 * @param jobStartTime
	 *            the jobStartTime to set
	 */
	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	/**
	 * @param jobEndTime
	 *            the jobEndTime to set
	 */
	public void setJobEndTime(Date jobEndTime) {
		this.jobEndTime = jobEndTime;
	}

}
