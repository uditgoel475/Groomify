package com.niit.lookatme.facade;

import java.util.List;

import com.niit.lookatme.customer.dto.CreateEmployeeInput;
import com.niit.lookatme.dao.Employee;

public interface EmployeeFacade {

	List<Employee> fetchAllExistingEmployeeCurrentWeekBirthdays();

	String createNewEmployee(CreateEmployeeInput createEmployeeInput);

}
