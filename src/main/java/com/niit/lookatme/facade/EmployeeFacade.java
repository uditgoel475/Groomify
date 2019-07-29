package com.niit.lookatme.facade;

import java.util.List;

import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.employee.dto.EmployeeInput;

public interface EmployeeFacade {

	List<Employee> fetchAllExistingEmployeeCurrentWeekBirthdays();

	String createNewEmployee(EmployeeInput createEmployeeInput);

}
