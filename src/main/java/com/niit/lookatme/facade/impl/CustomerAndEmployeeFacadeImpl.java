package com.niit.lookatme.facade.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.facade.CustomerAndEmployeeFacade;

@Service("customerAndEmployeeFacade")
public class CustomerAndEmployeeFacadeImpl implements CustomerAndEmployeeFacade {

	@Resource
	private CustomerRepository  customerRepository;

	@Resource
	private EmployeeRepository employeeRepository;

	@Override
	public CustomerAndEmployeeDataDTO fetchAllCustomerAndEmployeeBirthdays() {
		
		Date startDate = Date.from((LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))).atStartOfDay()
			      .atZone(ZoneId.systemDefault())
			      .toInstant());
		Date endDate = Date.from(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atStartOfDay()
			      .atZone(ZoneId.systemDefault())
			      .toInstant());

		List<Customer> customersWithBirthdayWeek = customerRepository.findAllByDobBetweenOrderByDobAsc(startDate, endDate);
		
		List<Employee> employeesWithBirthdayWeek = employeeRepository.findAllByDobBetweenAndLeavingDateGreaterThanOrEqualToOrderByDobAsc(startDate, endDate, Calendar.getInstance().getTime());
		
		return new CustomerAndEmployeeDataDTO(employeesWithBirthdayWeek, customersWithBirthdayWeek);
	}

	@Override
	public String createNewEmployee(Employee employee) {
		employee.setUsername(createEmployeeId(employee));
		Employee empUpdate = employeeRepository.save(employee);
		if(empUpdate.getId() != null) {
			return empUpdate.getUsername();
		}
		return "";
	}
	
	private String createEmployeeId(Employee employee) {
		long employeecount = employeeRepository.count();
	    String initString = employee.getFname().substring(0, 3) + (employee.getMname().isEmpty() ? "0" : employee.getMname().substring(0, 1)) + employee.getLname().substring(0, 3);
	    initString = StringUtils.rightPad(initString, 13, '0');
	    return StringUtils.rightPad(initString, 13, String.valueOf(employeecount));
	}
	
	private String createCustomerId(Customer customer) {
		long employeecount = employeeRepository.count();
	    String initString = customer.getFname().substring(0, 3) + (customer.getMname().isEmpty() ? "0" : customer.getMname().substring(0, 1)) + customer.getLname().substring(0, 3);
	    initString = StringUtils.rightPad(initString, 13, '0');
	    return StringUtils.rightPad(initString, 13, String.valueOf(employeecount));
	}

	@Override
	public String createNewCustomer(Customer customer) {
		if(StringUtils.isEmpty(customer.getUsername()))
			customer.setUsername(createCustomerId(customer));
		Customer custUpdate = customerRepository.save(customer);
		if(custUpdate.getId() != null) {
			return custUpdate.getUsername();
		}
		return "";
	}

}
