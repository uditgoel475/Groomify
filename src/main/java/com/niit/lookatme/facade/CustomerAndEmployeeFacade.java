package com.niit.lookatme.facade;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;

public interface CustomerAndEmployeeFacade {

	CustomerAndEmployeeDataDTO fetchAllCustomerAndEmployeeBirthdays();

	String createNewEmployee(Employee employee);

	String createNewCustomer(Customer customer);
}
