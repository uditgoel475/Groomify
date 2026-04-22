package com.uditgoel.groomify.dto.customer;

import java.util.Date;
import java.util.List;

import com.uditgoel.groomify.dao.JobStatus;

public class CustomerOrderOut {

	private String orderId;
	private JobStatus requestStatus;
	private CustomerOutDTO customerOutDTO;
	private Date appointmentDate;
	private List<CustomerJobCardOut> customerJobCardList;

	public CustomerOrderOut() {super();}
	
	public CustomerOrderOut(String orderId, JobStatus requestStatus,
			CustomerOutDTO customerOutDTO, Date appointmentDate,
			List<CustomerJobCardOut> customerJobCardList) {
		super();
		this.orderId = orderId;
		this.requestStatus = requestStatus;
		this.customerOutDTO = customerOutDTO;
		this.appointmentDate = appointmentDate;
		this.customerJobCardList = customerJobCardList;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @return the requestStatus
	 */
	public JobStatus getRequestStatus() {
		return requestStatus;
	}

	/**
	 * @return the customerOutDTO
	 */
	public CustomerOutDTO getCustomerOutDTO() {
		return customerOutDTO;
	}

	/**
	 * @return the appointmentDate
	 */
	public Date getAppointmentDate() {
		return appointmentDate;
	}

	/**
	 * @return the customerJobCardList
	 */
	public List<CustomerJobCardOut> getCustomerJobCardList() {
		return customerJobCardList;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @param requestStatus
	 *            the requestStatus to set
	 */
	public void setRequestStatus(JobStatus requestStatus) {
		this.requestStatus = requestStatus;
	}

	/**
	 * @param customerOutDTO
	 *            the customerOutDTO to set
	 */
	public void setCustomerOutDTO(CustomerOutDTO customerOutDTO) {
		this.customerOutDTO = customerOutDTO;
	}

	/**
	 * @param appointmentDate
	 *            the appointmentDate to set
	 */
	public void setAppointmentDate(Date appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	/**
	 * @param customerJobCardList
	 *            the customerJobCardList to set
	 */
	public void setCustomerJobCardList(List<CustomerJobCardOut> customerJobCardList) {
		this.customerJobCardList = customerJobCardList;
	}
}
