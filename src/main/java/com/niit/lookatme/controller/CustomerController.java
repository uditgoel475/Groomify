package com.niit.lookatme.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dto.CreateCustomerOrderInput;
import com.niit.lookatme.employee.dto.CustomerInput;
import com.niit.lookatme.employee.dto.UpdateCustomerOrderInput;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@RestController
@RequestMapping("api/employee")
public class CustomerController {

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@PostMapping("upload/profile/{custNo}")
	public ResponseEntity<Boolean> uploadCustomerImageProfile(@RequestParam("file") MultipartFile file,
			@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, custNo, UserImageInputType.PROFILE)));
	}

	@PostMapping("upload/govt/{custNo}")
	public ResponseEntity<Boolean> uploadCustomerImageGovt(@RequestParam("file") MultipartFile file,
			@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, custNo, UserImageInputType.GOVTID)));
	}

	@PostMapping("create")
	public ResponseEntity<String> createCustomer(@RequestBody CustomerInput customerInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomer(customerInput));
	}

	@PostMapping("changepwd/{custNo}")
	public ResponseEntity<Boolean> updateCustomerPassword(@PathVariable("custNo") String custNo,
			@RequestBody Map<String, String> encryptedPassword) {
		return ResponseEntity.ok(customerFacade.changeCustomerPassword(custNo, encryptedPassword.get("custPass")));
	}

	@GetMapping("orders/open/{custNo}")
	public ResponseEntity<List<CustomerOrder>> fetchAllOpenCustomerOrders(@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(customerFacade.fetchAllOpenCustomerOrder(custNo));
	}

	@GetMapping("orders/all/{custNo}")
	public ResponseEntity<List<CustomerOrder>> fetchAllCustomerOrderGivenDate(@PathVariable("custNo") String custNo,
			@RequestHeader("date") Date date) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerCalendarOpenAppointmentGivenDate(custNo, date));
	}

	@GetMapping("enquiry/{custNo}")
	public ResponseEntity<List<CustomerOrder>> fetchAllCustomerEnquiryGivenDate(@PathVariable("custNo") String custNo,
			@RequestHeader("date") Date date) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerEnquiryGivenDate(custNo, date));
	}

	@GetMapping("enquiry/all/{custNo}")
	public ResponseEntity<List<CustomerOrder>> fetchAllCustomerEnquiries(@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerEnquiries(custNo));
	}

	@GetMapping("enquiry/all")
	public ResponseEntity<List<CustomerOrder>> fetchAllCustomerEnquiriesDateRange(
			@RequestHeader("startDate") Date startDate, @RequestHeader("endDate") Date endDate) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerEnquiriesDateRange(startDate, endDate));
	}

	@PostMapping("enquiry")
	public ResponseEntity<String> createNewEnquiry(@RequestBody CreateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomerEnquiry(customerOrderInput));
	}

	@PostMapping("initiateEnquiryToOrder")
	public ResponseEntity<Boolean> initiateEnquiryToOrder(@RequestBody UpdateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.initiateEnquiryToOrder(customerOrderInput));
	}
	
	@PostMapping("updateServices")
	public ResponseEntity<Boolean> updateServices(@RequestBody UpdateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.updateServices(customerOrderInput));
	}

	@PostMapping("cancelEntireOrder/{orderId}")
	public ResponseEntity<Boolean> cancelEntireOrder(@PathVariable("orderId") String orderId) {
		return ResponseEntity.ok(customerFacade.cancelEntireOrder(orderId));
	}

	@PostMapping("order")
	public ResponseEntity<String> createNewOrder(@RequestBody CreateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomerOrder(customerOrderInput));
	}
}
