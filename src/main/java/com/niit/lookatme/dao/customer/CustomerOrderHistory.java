package com.niit.lookatme.dao.customer;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.JobStatus;

@Entity
@Table(name = "CUSTOMER_ORDER_HISTORY")
public class CustomerOrderHistory extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -638162356135432921L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "COH_ID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "REQUEST_ID", nullable = false, unique = true)
	private String requestId;
	
	@Column(name = "REQUEST_STATUS")
	private JobStatus requestStatus;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "CUSTOMER_ID")
	private Customer customer;
	
	@OneToMany(mappedBy = "customerOrderHistory", cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
	private List<CustomerJobCardHistory> customerJobCards;

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
	 * @return the customerJobCards
	 */
	public List<CustomerJobCardHistory> getCustomerJobCards() {
		if(null == customerJobCards) {
			customerJobCards = new ArrayList<>();
		}
		return customerJobCards;
	}

	/**
	 * @param customerJobCards the customerJobCards to set
	 */
	public void setCustomerJobCards(List<CustomerJobCardHistory> customerJobCards) {
		this.customerJobCards = customerJobCards;
	}
	
	
}
