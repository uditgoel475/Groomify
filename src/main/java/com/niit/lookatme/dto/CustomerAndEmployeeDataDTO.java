package com.niit.lookatme.dto;

import java.util.List;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.employee.dao.Employee;

public class CustomerAndEmployeeDataDTO {

	List<Employee> employeeList;
	List<Customer> customerList;
	public CustomerAndEmployeeDataDTO(List<Employee> employeeList, List<Customer> customerList) {
		super();
		this.employeeList = employeeList;
		this.customerList = customerList;
	}
	/**
	 * @return the employeeList
	 */
	public List<Employee> getEmployeeList() {
		return employeeList;
	}
	/**
	 * @return the customerList
	 */
	public List<Customer> getCustomerList() {
		return customerList;
	}
	/**
	 * @param employeeList the employeeList to set
	 */
	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
	/**
	 * @param customerList the customerList to set
	 */
	public void setCustomerList(List<Customer> customerList) {
		this.customerList = customerList;
	}
	
}
