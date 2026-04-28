package com.uditgoel.groomify.controller;

import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uditgoel.groomify.dto.CustomerAndEmployeeDataDTO;
import com.uditgoel.groomify.dto.customer.CustomerOrderOut;
import com.uditgoel.groomify.dto.customer.CustomerOutDTO;
import com.uditgoel.groomify.dto.employee.EmployeeDTO;
import com.uditgoel.groomify.facade.CustomerFacade;
import com.uditgoel.groomify.facade.EmployeeFacade;
import com.uditgoel.groomify.facade.helper.EmployeeFacadeHelper;

@RestController
@RequestMapping("/api/customeremployee")
public class CustomerAndEmployeeController {

	private final EmployeeFacade employeeFacade;

	private final CustomerFacade customerFacade;

	@SuppressWarnings("unused")
	private final EmployeeFacadeHelper employeeFacadeHelper;

	public CustomerAndEmployeeController(@Qualifier("employeeFacade") EmployeeFacade employeeFacade,
			@Qualifier("customerFacade") CustomerFacade customerFacade,
			EmployeeFacadeHelper employeeFacadeHelper) {
		this.employeeFacade = employeeFacade;
		this.customerFacade = customerFacade;
		this.employeeFacadeHelper = employeeFacadeHelper;
	}

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
