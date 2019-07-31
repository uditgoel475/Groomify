package com.niit.lookatme.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.facade.EmployeeFacade;

@RestController
@RequestMapping("api/customeremployee")
public class CustomerAndEmployeeController {

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

	@GetMapping("calendarorders/open/{year}/{month}")
	public ResponseEntity<List<CustomerOrder>> fetchAllOpenCustomerOrderGivenMonthYear(@PathVariable("year") int year,
			@PathVariable("month") String month) {
		return ResponseEntity.ok(customerFacade.fetchAllCalendarOpenAppointmentCurrentMonth(year, month));
	}
	
	@GetMapping("enquiry/all")
	public ResponseEntity<List<CustomerOrder>> fetchAllEnquiriesGivenDate(@RequestHeader("date") Date date) {
		return ResponseEntity.ok(customerFacade.fetchAllEnquiriesGivenDate(date));
	}
}
