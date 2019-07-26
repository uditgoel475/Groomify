package com.niit.lookatme.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.service.CustomerAndEmployeeService;

@RestController
@RequestMapping("api/customeremployee")
public class CustomerAndEmployeeDetailsController {

	@Resource(name = "customerAndEmployeeService")
	private CustomerAndEmployeeService customerAndEmployeeService;
	
	@GetMapping("birthdayweek")
	public ResponseEntity<CustomerAndEmployeeDataDTO> fetchBirthdayWeekUserAndEmployee() {
		return ResponseEntity.ok(customerAndEmployeeService.fetchAllCustomerAndEmployeeBirthdays());
	}
}
