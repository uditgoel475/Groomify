package com.niit.lookatme.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.facade.EmployeeFacade;

@RestController
@RequestMapping("api/customeremployee")
public class CustomerAndEmployeeDetailsController {

	@Resource(name = "employeeFacade")
	private EmployeeFacade employeeFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@GetMapping("birthdayweek")
	public ResponseEntity<CustomerAndEmployeeDataDTO> fetchBirthdayWeekUserAndEmployee() {
		List<Employee> employeeList = employeeFacade.fetchAllExistingEmployeeCurrentWeekBirthdays();
		List<Customer> customerList = customerFacade.fetchAllCustomerCurrentWeekBirthdays();
		return ResponseEntity.ok(new CustomerAndEmployeeDataDTO(employeeList, customerList));
	}
}
