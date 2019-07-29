package com.niit.lookatme.facade;

import java.util.List;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.employee.dto.CustomerInput;

public interface CustomerFacade {
	
	List<Customer> fetchAllCustomerCurrentWeekBirthdays();

	String createNewCustomer(CustomerInput customerInput);

	Boolean changeCustomerPassword(String custNo, String encryptedPassword);
}
