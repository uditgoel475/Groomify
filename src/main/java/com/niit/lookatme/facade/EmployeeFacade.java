package com.niit.lookatme.facade;

import java.util.List;

import com.niit.lookatme.dao.Activity;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.employee.dto.EmployeeInput;

public interface EmployeeFacade {

	List<Employee> fetchAllExistingEmployeeCurrentWeekBirthdays();

	String createNewEmployee(EmployeeInput createEmployeeInput);

	Boolean changeEmployeePassword(String empNo, String encryptedPassword);

	Boolean markActivity(String empNo, Activity activity, String custUsername);

}
