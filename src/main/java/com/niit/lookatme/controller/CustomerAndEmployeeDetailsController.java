package com.niit.lookatme.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.facade.CustomerAndEmployeeFacade;

@RestController
@RequestMapping("api/customeremployee")
public class CustomerAndEmployeeDetailsController {

	@Resource(name = "customerAndEmployeeFacade")
	private CustomerAndEmployeeFacade customerAndEmployeeFacade;
	
	@GetMapping("birthdayweek")
	public ResponseEntity<CustomerAndEmployeeDataDTO> fetchBirthdayWeekUserAndEmployee() {
		return ResponseEntity.ok(customerAndEmployeeFacade.fetchAllCustomerAndEmployeeBirthdays());
	}
	
	@PostMapping("create/employee")
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
		return ResponseEntity.ok(customerAndEmployeeFacade.createNewEmployee(employee));
	}
	
	@PostMapping("create/customer")
	public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
		return ResponseEntity.ok(customerAndEmployeeFacade.createNewCustomer(customer));
	}
}
