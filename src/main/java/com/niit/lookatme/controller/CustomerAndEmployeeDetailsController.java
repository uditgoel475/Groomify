package com.niit.lookatme.controller;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dto.CreateEmployeeInput;
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
	
	@PostMapping("employee/create")
	public ResponseEntity<String> createEmployee(@RequestBody CreateEmployeeInput createEmployeeInput) {
		return ResponseEntity.ok(customerAndEmployeeFacade.createNewEmployee(createEmployeeInput));
	}
	
	@PostMapping("employee/upload/profile/{empNo}")
	public ResponseEntity<Boolean> uploadEmployeeImageProfile(@RequestParam("file") MultipartFile file, @PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(customerAndEmployeeFacade.uploadPictureImage(file, empNo, "profile"));
	}
	
	@PostMapping("employee/upload/govt/{empNo}")
	public ResponseEntity<Boolean> uploadEmployeeImageGovt(@RequestParam("file") MultipartFile file, @PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(customerAndEmployeeFacade.uploadPictureImage(file, empNo, "govtId"));
	}
	
	@PostMapping("create/customer")
	public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
		return ResponseEntity.ok(customerAndEmployeeFacade.createNewCustomer(customer));
	}
}
