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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.dto.customer.CustomerOrderOut;
import com.niit.lookatme.dto.customer.CustomerOutDTO;
import com.niit.lookatme.dto.employee.EmployeeDTO;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.facade.helper.EmployeeFacadeHelper;

@RestController
@RequestMapping("api/customeremployee")
public class CustomerAndEmployeeController {

	@Resource(name = "employeeFacade")
	private EmployeeFacade employeeFacade;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	
	@Resource
	private EmployeeFacadeHelper employeeFacadeHelper;

	@GetMapping("birthdayweek")
	public ResponseEntity<CustomerAndEmployeeDataDTO> fetchBirthdayWeekUserAndEmployee() {
		List<EmployeeDTO> employeeList = employeeFacade.fetchAllExistingEmployeeCurrentWeekBirthdays();
		List<CustomerOutDTO> customerList = customerFacade.fetchAllCustomerCurrentWeekBirthdays();
		return ResponseEntity.ok(new CustomerAndEmployeeDataDTO(employeeList, customerList));
	}

	@GetMapping("calendarorders/open/{year}/{month}")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllOpenCustomerOrderGivenMonthYear(@PathVariable("year") Year year,
			@PathVariable("month") Month month) {
		return ResponseEntity.ok(customerFacade.fetchAllCalendarOpenAppointmentCurrentMonth(year.getValue(), month));
	}

	@GetMapping("enquiry/all")
	public ResponseEntity<List<CustomerOrderOut>> fetchAllEnquiriesGivenDate(
			@RequestHeader("enquiryDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date enquiryDate) {
		return ResponseEntity.ok(customerFacade.fetchAllEnquiriesGivenDate(enquiryDate));
	}
}
