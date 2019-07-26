package com.niit.lookatme.service.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.service.CustomerAndEmployeeService;

@Service("customerAndEmployeeService")
public class CustomerAndEmployeeServiceImpl implements CustomerAndEmployeeService {

	@Resource(name = "customerRepository")
	private CustomerRepository<Customer, Long> customerRepository;

	@Resource(name = "employeeRepository")
	private EmployeeRepository<Employee, Long> employeeRepository;

	@Override
	public CustomerAndEmployeeDataDTO fetchAllCustomerAndEmployeeBirthdays() {

		List<Customer> customersWithBirthdayWeek = customerRepository.findCustomersByBirthdayCurrentWeek();
		Collections.sort(customersWithBirthdayWeek,
				(Customer customer1, Customer customer2) -> customer1.getDob().compareTo(customer2.getDob()));
		List<Employee> employeesWithBirthdayWeek = employeeRepository.findEmployeesByBirthdayCurrentWeek();
		Collections.sort(employeesWithBirthdayWeek,
				(Employee employee1, Employee employee2) -> employee1.getDob().compareTo(employee2.getDob()));

		return new CustomerAndEmployeeDataDTO(employeesWithBirthdayWeek, customersWithBirthdayWeek);
	}

}
