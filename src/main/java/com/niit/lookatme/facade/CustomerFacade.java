package com.niit.lookatme.facade;

import java.time.Month;
import java.util.Date;
import java.util.List;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.employee.dto.CreateCustomerOrderInput;
import com.niit.lookatme.employee.dto.CustomerDTO;
import com.niit.lookatme.employee.dto.UpdateCustomerOrderInput;

public interface CustomerFacade {
	
	List<Customer> fetchAllCustomerCurrentWeekBirthdays();

	String createNewCustomer(CustomerDTO customerInput);

	Boolean changeCustomerPassword(String custNo, String encryptedPassword);

	List<CustomerOrder> fetchAllOpenCustomerOrder(String username);

	List<CustomerOrder> fetchAllCalendarOpenAppointmentCurrentMonth(int year, Month month);

	List<CustomerOrder> fetchAllCustomerCalendarOpenAppointmentGivenDate(String custNo, Date date);

	List<CustomerOrder> fetchAllCustomerEnquiryGivenDate(String custNo, Date date);

	List<CustomerOrder> fetchAllCustomerEnquiries(String custNo);

	List<CustomerOrder> fetchAllEnquiriesGivenDate(Date date);

	String createNewCustomerEnquiry(CreateCustomerOrderInput enquiryInput);

	List<CustomerOrder> fetchAllCustomerEnquiriesGivenMonth(Month month, int year);

	String createNewCustomerOrder(CreateCustomerOrderInput customerOrderInput);

	Boolean initiateEnquiryToOrder(UpdateCustomerOrderInput customerOrderInput);

	Boolean cancelEntireOrder(String requestId);

	Boolean updateServices(UpdateCustomerOrderInput customerOrderInput);
}
