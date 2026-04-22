package com.uditgoel.groomify.dto;

import java.util.List;

import com.uditgoel.groomify.dto.customer.CustomerOutDTO;
import com.uditgoel.groomify.dto.employee.EmployeeDTO;

public class CustomerAndEmployeeDataDTO {

	List<EmployeeDTO> employeeList;
	List<CustomerOutDTO> customerList;

	public CustomerAndEmployeeDataDTO() {super();}
	public CustomerAndEmployeeDataDTO(List<EmployeeDTO> employeeList2,
			List<CustomerOutDTO> customerList2) {
		super();
		this.employeeList = employeeList2;
		this.customerList = customerList2;
	}

	/**
	 * @return the employeeList
	 */
	public List<EmployeeDTO> getEmployeeList() {
		return employeeList;
	}

	/**
	 * @return the customerList
	 */
	public List<CustomerOutDTO> getCustomerList() {
		return customerList;
	}

	/**
	 * @param employeeList
	 *            the employeeList to set
	 */
	public void setEmployeeList(List<EmployeeDTO> employeeList) {
		this.employeeList = employeeList;
	}

	/**
	 * @param customerList
	 *            the customerList to set
	 */
	public void setCustomerList(List<CustomerOutDTO> customerList) {
		this.customerList = customerList;
	}

}
