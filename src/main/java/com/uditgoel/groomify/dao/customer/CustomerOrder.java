package com.uditgoel.groomify.dao.customer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.uditgoel.groomify.dao.AuditInfo;
import com.uditgoel.groomify.dao.JobStatus;

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
	
	@OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL)
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
