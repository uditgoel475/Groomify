package com.niit.lookatme.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
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
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dto.CustomerInput;
import com.niit.lookatme.employee.dto.EmployeeInput;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

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

	@PostMapping("employee/create")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeInput employeeInput) {
		return ResponseEntity.ok(employeeFacade.createNewEmployee(employeeInput));
	}

	@PostMapping("employee/upload/profile/{empNo}")
	public ResponseEntity<Boolean> uploadEmployeeImageProfile(@RequestParam("file") MultipartFile file,
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.EMPLOYEE,
				file, empNo, UserImageInputType.PROFILE)));
	}

	@PostMapping("employee/upload/govt/{empNo}")
	public ResponseEntity<Boolean> uploadEmployeeImageGovt(@RequestParam("file") MultipartFile file,
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.EMPLOYEE,
				file, empNo, UserImageInputType.GOVTID)));
	}

	@PostMapping("customer/upload/profile/{empNo}")
	public ResponseEntity<Boolean> uploadCustomerImageProfile(@RequestParam("file") MultipartFile file,
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, empNo, UserImageInputType.PROFILE)));
	}

	@PostMapping("customer/upload/govt/{empNo}")
	public ResponseEntity<Boolean> uploadCustomerImageGovt(@RequestParam("file") MultipartFile file,
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, empNo, UserImageInputType.GOVTID)));
	}

	@PostMapping("create/customer")
	public ResponseEntity<String> createCustomer(@RequestBody CustomerInput customerInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomer(customerInput));
	}

	@PostMapping("update/customerPassword/{custNo}")
	public ResponseEntity<Boolean> updateCustomerPassword(@PathVariable("custNo") String custNo,
			@RequestBody Map<String, String> encryptedPassword) {
		return ResponseEntity.ok(customerFacade.updateCustomerPassword(custNo, encryptedPassword.get("custPass")));
	}
}
