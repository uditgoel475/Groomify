package com.niit.lookatme.customer.dto;

import java.util.Date;
import java.util.List;

import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.dao.PaymentModes;

public class CustomerJobCardOut {

	private String jobCardId;
	private JobStatus jobCardStatus;
	private String customerFeedback;
	private Date jobStartTime;
	private Date jobEndTime;
	private PaymentModes paymentMode;
	private Long paymentAmount;
	private Long paidAmount;
	private String paymentComments;
	private List<CustomerJobCardDetailsOut> customerJobCardDetailsOutList;

	public CustomerJobCardOut(String jobCardId, JobStatus jobCardStatus, Date jobStartTime, Date jobEndTime,
			PaymentModes paymentMode, Long paymentAmount,
			List<CustomerJobCardDetailsOut> customerJobCardDetailsOutList) {
		super();
		this.jobCardId = jobCardId;
		this.jobCardStatus = jobCardStatus;
		this.jobStartTime = jobStartTime;
		this.jobEndTime = jobEndTime;
		this.paymentMode = paymentMode;
		this.paymentAmount = paymentAmount;
		this.customerJobCardDetailsOutList = customerJobCardDetailsOutList;
	}

	/**
	 * @return the jobCardId
	 */
	public String getJobCardId() {
		return jobCardId;
	}

	/**
	 * @return the jobCardStatus
	 */
	public JobStatus getJobCardStatus() {
		return jobCardStatus;
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
	 * @return the customerJobCardDetailsOutList
	 */
	public List<CustomerJobCardDetailsOut> getCustomerJobCardDetailsOutList() {
		return customerJobCardDetailsOutList;
	}

	/**
	 * @param jobCardId
	 *            the jobCardId to set
	 */
	public void setJobCardId(String jobCardId) {
		this.jobCardId = jobCardId;
	}

	/**
	 * @param jobCardStatus
	 *            the jobCardStatus to set
	 */
	public void setJobCardStatus(JobStatus jobCardStatus) {
		this.jobCardStatus = jobCardStatus;
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

	/**
	 * @param paymentMode
	 *            the paymentMode to set
	 */
	public void setPaymentMode(PaymentModes paymentMode) {
		this.paymentMode = paymentMode;
	}

	/**
	 * @param paymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(Long paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	/**
	 * @param paidAmount
	 *            the paidAmount to set
	 */
	public void setPaidAmount(Long paidAmount) {
		this.paidAmount = paidAmount;
	}

	/**
	 * @param paymentComments
	 *            the paymentComments to set
	 */
	public void setPaymentComments(String paymentComments) {
		this.paymentComments = paymentComments;
	}

	/**
	 * @param customerJobCardDetailsOutList
	 *            the customerJobCardDetailsOutList to set
	 */
	public void setCustomerJobCardDetailsOutList(List<CustomerJobCardDetailsOut> customerJobCardDetailsOutList) {
		this.customerJobCardDetailsOutList = customerJobCardDetailsOutList;
	}
}
