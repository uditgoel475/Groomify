package com.niit.lookatme.customer.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.services.dao.Service;

@Entity
@Table(name = "JOB_CARD_DETAILS")
public class CustomerJobCardDetails extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8521184650855457805L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAH_ID", updatable = false, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_CARD", referencedColumnName = "JOB_CARD_ID")
	private CustomerJobCard jobId;
	
	@Column(name = "SUB_JOB_ID", nullable = false, unique = true)
	private String subJobId;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "JOB_STATUS", nullable = false)
	private JobStatus jobStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE", referencedColumnName = "EMPLOYEE_ID")
	private Employee activityEmployee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE", referencedColumnName = "SERVICE_ID")
	private Service service;

	@Column(name = "CUSTOMER_FEEDBACK")
	private String customerFeedback;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "JOB_START_TIME")
	private Date jobStartTime;
	
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "JOB_END_TIME")
	private Date jobEndTime;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the jobId
	 */
	public CustomerJobCard getJobId() {
		return jobId;
	}

	/**
	 * @return the jobStatus
	 */
	public JobStatus getJobStatus() {
		return jobStatus;
	}

	/**
	 * @return the activityEmployee
	 */
	public Employee getActivityEmployee() {
		return activityEmployee;
	}

	/**
	 * @return the service
	 */
	public Service getService() {
		return service;
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
	 * @param jobId the jobId to set
	 */
	public void setJobId(CustomerJobCard jobId) {
		this.jobId = jobId;
	}

	/**
	 * @return the subJobId
	 */
	public String getSubJobId() {
		return subJobId;
	}

	/**
	 * @param subJobId the subJobId to set
	 */
	public void setSubJobId(String subJobId) {
		this.subJobId = subJobId;
	}

	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
	}

	/**
	 * @param activityEmployee the activityEmployee to set
	 */
	public void setActivityEmployee(Employee activityEmployee) {
		this.activityEmployee = activityEmployee;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @param customerFeedback the customerFeedback to set
	 */
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}

	/**
	 * @param jobStartTime the jobStartTime to set
	 */
	public void setJobStartTime(Date jobStartTime) {
		this.jobStartTime = jobStartTime;
	}

	/**
	 * @param jobEndTime the jobEndTime to set
	 */
	public void setJobEndTime(Date jobEndTime) {
		this.jobEndTime = jobEndTime;
	}
}
