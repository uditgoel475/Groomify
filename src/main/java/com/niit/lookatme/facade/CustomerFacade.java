package com.niit.lookatme.facade;

import java.time.Month;
import java.util.Date;
import java.util.List;

import com.niit.lookatme.customer.dto.CreateCustomerOrderInput;
import com.niit.lookatme.customer.dto.CustomerDTO;
import com.niit.lookatme.customer.dto.CustomerOrderOut;
import com.niit.lookatme.customer.dto.CustomerOutDTO;
import com.niit.lookatme.customer.dto.UpdateCustomerOrderInput;

public interface CustomerFacade {
	
	List<CustomerOutDTO> fetchAllCustomerCurrentWeekBirthdays();

	String createNewCustomer(CustomerDTO customerInput);

	Boolean changeCustomerPassword(String custNo, String encryptedPassword);

	List<CustomerOrderOut> fetchAllOpenCustomerOrder(String username);

	List<CustomerOrderOut> fetchAllCalendarOpenAppointmentCurrentMonth(int year, Month month);

	List<CustomerOrderOut> fetchAllCustomerCalendarOpenAppointmentGivenDate(String custNo, Date date);

	List<CustomerOrderOut> fetchAllCustomerEnquiryGivenDate(String custNo, Date date);

	List<CustomerOrderOut> fetchAllCustomerEnquiries(String custNo);

	List<CustomerOrderOut> fetchAllEnquiriesGivenDate(Date date);

	String createNewCustomerEnquiry(CreateCustomerOrderInput enquiryInput);

	List<CustomerOrderOut> fetchAllCustomerEnquiriesGivenMonth(Month month, int year);

	String createNewCustomerOrder(CreateCustomerOrderInput customerOrderInput);

	Boolean initiateEnquiryToOrder(UpdateCustomerOrderInput customerOrderInput);

	Boolean cancelEntireOrder(String requestId);

	Boolean updateServices(UpdateCustomerOrderInput customerOrderInput);
}
