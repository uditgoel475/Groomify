package com.uditgoel.groomify.facade;

import java.time.Month;
import java.util.Date;
import java.util.List;

import com.uditgoel.groomify.dto.customer.CreateCustomerOrderInput;
import com.uditgoel.groomify.dto.customer.CustomerDTO;
import com.uditgoel.groomify.dto.customer.CustomerOrderOut;
import com.uditgoel.groomify.dto.customer.CustomerOutDTO;
import com.uditgoel.groomify.dto.customer.UpdateCustomerOrderInput;

public interface CustomerFacade {
	
	List<CustomerOutDTO> fetchAllCustomerCurrentWeekBirthdays();

	String createNewCustomer(CustomerDTO customerInput);

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

	CustomerOutDTO fetchCustomerDTO(String username);

	Boolean changeCustomerPassword(String custNo, String currentPass, String newPass);

	Boolean checkEmailAvailability(String email);

	Boolean checkUsernameAvailability(String username);

	List<CustomerOutDTO> findAllMatchingName(String name);
}
