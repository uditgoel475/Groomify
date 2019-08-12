package com.niit.lookatme.controller;

import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.customer.dto.CreateCustomerOrderInput;
import com.niit.lookatme.customer.dto.CustomerOrderOut;
import com.niit.lookatme.customer.dto.UpdateCustomerOrderInput;
import com.niit.lookatme.facade.CustomerFacade;

@RestController
@RequestMapping("api/customerorder")
public class CustomerOrderController {

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	
	@GetMapping("orders/open/{custNo}")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllOpenCustomerOrders(@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(customerFacade.fetchAllOpenCustomerOrder(custNo));
	}

	@GetMapping("orders/all/{custNo}")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllCustomerOrderGivenDate(@PathVariable("custNo") String custNo,
			@RequestHeader("appointmentDate")  @DateTimeFormat(pattern="yyyy-MM-dd") Date appointmentDate) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerCalendarOpenAppointmentGivenDate(custNo, appointmentDate));
	}

	@GetMapping("enquiry/{custNo}")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllCustomerEnquiryGivenDate(@PathVariable("custNo") String custNo,
			@RequestHeader("appointmentDate")  @DateTimeFormat(pattern="yyyy-MM-dd") Date appointmentDate) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerEnquiryGivenDate(custNo, appointmentDate));
	}

	@GetMapping("enquiry/all/{custNo}")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllCustomerEnquiries(@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerEnquiries(custNo));
	}

	@GetMapping("enquiry/all/{month}/{year}")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllCustomerEnquiriesDateRange(@PathVariable("year") Year year, @PathVariable("month") Month month) {
		return ResponseEntity.ok(customerFacade.fetchAllCustomerEnquiriesGivenMonth(month, year.getValue()));
	}
	
	@PostMapping("enquiry/new")
	public ResponseEntity<String> createNewEnquiry(@RequestBody CreateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomerEnquiry(customerOrderInput));
	}

	@PostMapping("enquiry/initiate")
	public ResponseEntity<Boolean> initiateEnquiryToOrder(@RequestBody UpdateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.initiateEnquiryToOrder(customerOrderInput));
	}
	
	@PostMapping("updateServices")
	public ResponseEntity<Boolean> updateServices(@RequestBody UpdateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.updateServices(customerOrderInput));
	}

	@PostMapping("order/cancel/{orderId}")
	public ResponseEntity<Boolean> cancelEntireOrder(@PathVariable("orderId") String orderId) {
		return ResponseEntity.ok(customerFacade.cancelEntireOrder(orderId));
	}

	@PostMapping("order/new")
	public ResponseEntity<String> createNewOrder(@RequestBody CreateCustomerOrderInput customerOrderInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomerOrder(customerOrderInput));
	}
}
