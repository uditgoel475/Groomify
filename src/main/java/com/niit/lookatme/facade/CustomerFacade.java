package com.niit.lookatme.facade;

import java.util.List;

import com.niit.lookatme.customer.dao.Customer;

public interface CustomerFacade {
	
	List<Customer> fetchAllCustomerCurrentWeekBirthdays();

	String createNewCustomer(Customer customer);
}
