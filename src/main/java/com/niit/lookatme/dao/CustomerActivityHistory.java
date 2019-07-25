package com.niit.lookatme.dao;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.niit.lookatme.services.dao.Service;

@Entity
@Table(name = "Customer_Activity_History")
public class CustomerActivityHistory extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8465026238692191587L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CAH_ID", updatable = false, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "CUSTOMER_ID")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE", referencedColumnName = "EMPLOYEE_ID")
	private Employee activityEmployee;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE", referencedColumnName = "SERVICE_ID")
	private Service service;
	
	@Temporal(value = TemporalType.TIME)
	@Column(name = "TIME_CONSUMED")
	private Time timeConsumed;
	
	@Column(name = "CUSTOMER_FEEDBACK")
	private String customerFeedback;
	
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
	 * @return the timeConsumed
	 */
	public Time getTimeConsumed() {
		return timeConsumed;
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
	 * @param timeConsumed the timeConsumed to set
	 */
	public void setTimeConsumed(Time timeConsumed) {
		this.timeConsumed = timeConsumed;
	}

	/**
	 * @param customerFeedback the customerFeedback to set
	 */
	public void setCustomerFeedback(String customerFeedback) {
		this.customerFeedback = customerFeedback;
	}

	
}
