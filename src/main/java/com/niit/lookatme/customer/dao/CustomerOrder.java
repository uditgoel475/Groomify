package com.niit.lookatme.customer.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.JobStatus;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class CustomerOrder extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6295321259673917791L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_ORDER_ID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "REQUEST_ID", nullable = false, unique = true)
	private String requestId;
	
	@Column(name = "REQUEST_STATUS")
	private JobStatus requestStatus;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "CUSTOMER_ID")
	private Customer customer;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "APPOINTMENT_DATE")
	private Date appointmentDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_INIT_DATE")
	private Date requestInitTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REQUEST_END_DATE")
	private Date requestEndTime;
	
	@OneToMany(mappedBy = "customerOrder", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
	private List<CustomerJobCard> customerJobCards;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @return the requestStatus
	 */
	public JobStatus getRequestStatus() {
		return requestStatus;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return the requestInitTime
	 */
	public Date getRequestInitTime() {
		return requestInitTime;
	}

	/**
	 * @return the requestEndTime
	 */
	public Date getRequestEndTime() {
		return requestEndTime;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(JobStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @param requestInitTime the requestInitTime to set
	 */
	public void setRequestInitTime(Date requestInitTime) {
		this.requestInitTime = requestInitTime;
	}

	/**
	 * @param requestEndTime the requestEndTime to set
	 */
	public void setRequestEndTime(Date requestEndTime) {
		this.requestEndTime = requestEndTime;
	}

	/**
	 * @return the appointmentDate
	 */
	public Date getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * @param appointmentDate the appointmentDate to set
	 */
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	/**
	 * @return the customerJobCards
	 */
	public List<CustomerJobCard> getCustomerJobCards() {
		if(null == customerJobCards) {
			customerJobCards = new ArrayList<>();
		}
		return customerJobCards;
	}

	/**
	 * @param customerJobCards the customerJobCards to set
	 */
	public void setCustomerJobCards(List<CustomerJobCard> customerJobCards) {
		this.customerJobCards = customerJobCards;
	}
}
