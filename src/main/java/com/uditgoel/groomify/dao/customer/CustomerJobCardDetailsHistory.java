package com.uditgoel.groomify.dao.customer;

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

import com.uditgoel.groomify.dao.AuditInfo;
import com.uditgoel.groomify.dao.JobStatus;
import com.uditgoel.groomify.dao.employee.Employee;
import com.uditgoel.groomify.dao.services.Service;

@Entity
@Table(name = "JOB_CARD_DETAILS_HISTORY")
public class CustomerJobCardDetailsHistory extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8465026238692191587L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAH_ID", updatable = false, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "JOB_CARD_HISTORY", referencedColumnName = "JOB_CARD_HISTORY_ID")
	private CustomerJobCardHistory jobId;
	
	@Column(name = "SUB_JOB_ID", nullable = false)
	private String subJobId;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "JOB_STATUS", nullable = false)
	private JobStatus jobStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "CUSTOMER_ID")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE", referencedColumnName = "EMPLOYEE_ID")
	private Employee activityEmployee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE", referencedColumnName = "SERVICE_ID")
	private Service service;
	
	@Column(name = "CUSTOMER_FEEDBACK")
	private String customerFeedback;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "JOB_START_TIME")
	private Date jobStartTime;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "JOB_END_TIME")
	private Date jobEndTime;
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
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
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
	 * @return the jobId
	 */
	public CustomerJobCardHistory getJobId() {
		return jobId;
	}

	/**
	 * @return the jobStatus
	 */
	public JobStatus getJobStatus() {
		return jobStatus;
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
	public void setJobId(CustomerJobCardHistory jobId) {
		this.jobId = jobId;
	}

	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
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

	
}
