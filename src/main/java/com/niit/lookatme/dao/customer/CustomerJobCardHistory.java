package com.niit.lookatme.dao.customer;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.niit.lookatme.dao.AuditInfo;
import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.dao.PaymentModes;

@Entity
@Table(name = "JOB_CARD_HISTORY")
public class CustomerJobCardHistory extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1040836256652650714L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "JOB_CARD_HISTORY_ID", updatable = false, nullable = false)
	private Long id;
	
	@Column(name = "JOB_ID", nullable = false, updatable = false)
	private String jobId;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUSTOMER_ORDER_HISTORY", referencedColumnName = "COH_ID")
	private CustomerOrderHistory customerOrderHistory;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "JOB_STATUS", nullable = false)
	private JobStatus jobStatus;

	@Column(name = "CUSTOMER_FEEDBACK")
	private String customerFeedback;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "JOB_START_TIME")
	private Date jobStartTime;
	
	@Temporal(value = TemporalType.DATE)
	@Column(name = "JOB_END_TIME")
	private Date jobEndTime;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "PAYMENT_MODE", nullable = false)
	private PaymentModes paymentMode;

	@Column(name = "PAYMENT_AMOUNT")
	private Long paymentAmount;
	
	@Column(name = "PAID_AMOUNT")
	private Long paidAmount;
	
	@Column(name = "PAYMENT_COMMENTS")
	private String paymentComments;

	@Column(name = "INVOICE_URL")
	private String invoiceUrl;
	
	@OneToMany(mappedBy = "jobId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
	private List<CustomerJobCardDetailsHistory> customerJobCardDetailsHistory;



	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the jobId
	 */
	public String getJobId() {
		return jobId;
	}

	/**
	 * @return the jobStatus
	 */
	public JobStatus getJobStatus() {
		return jobStatus;
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
	 * @return the paymentMode
	 */
	public PaymentModes getPaymentMode() {
		return paymentMode;
	}

	/**
	 * @return the paymentAmount
	 */
	public Long getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @return the paidAmount
	 */
	public Long getPaidAmount() {
		return paidAmount;
	}

	/**
	 * @return the paymentComments
	 */
	public String getPaymentComments() {
		return paymentComments;
	}

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	/**
	 * @param jobStatus the jobStatus to set
	 */
	public void setJobStatus(JobStatus jobStatus) {
		this.jobStatus = jobStatus;
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

	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(PaymentModes paymentMode) {
		this.paymentMode = paymentMode;
	}

	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * @param paidAmount the paidAmount to set
	 */
	public void setPaidAmount(Long paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @param paymentComments the paymentComments to set
	 */
	public void setPaymentComments(String paymentComments) {
		this.paymentComments = paymentComments;
	}

	/**
	 * @return the invoiceUrl
	 */
	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	/**
	 * @param invoiceUrl the invoiceUrl to set
	 */
	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	/**
	 * @return the customerOrderHistory
	 */
	public CustomerOrderHistory getCustomerOrderHistory() {
		return customerOrderHistory;
	}

	/**
	 * @param customerOrderHistory the customerOrderHistory to set
	 */
	public void setCustomerOrderHistory(CustomerOrderHistory customerOrderHistory) {
		this.customerOrderHistory = customerOrderHistory;
	}

	/**
	 * @return the customerJobCardDetailsHistory
	 */
	public List<CustomerJobCardDetailsHistory> getCustomerJobCardDetailsHistory() {
		return customerJobCardDetailsHistory;
	}

	/**
	 * @param customerJobCardDetailsHistory the customerJobCardDetailsHistory to set
	 */
	public void setCustomerJobCardDetailsHistory(List<CustomerJobCardDetailsHistory> customerJobCardDetailsHistory) {
		this.customerJobCardDetailsHistory = customerJobCardDetailsHistory;
	}
}
