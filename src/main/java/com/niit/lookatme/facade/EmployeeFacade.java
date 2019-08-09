package com.niit.lookatme.facade;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.niit.lookatme.dao.Activity;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.employee.dao.EmployeeDailyActivities;
import com.niit.lookatme.employee.dto.EmployeeInput;

public interface EmployeeFacade {

	List<Employee> fetchAllExistingEmployeeCurrentWeekBirthdays();

	String createNewEmployee(EmployeeInput createEmployeeInput);

	Boolean changeEmployeePassword(String empNo, String encryptedPassword);

	Boolean markActivity(String empNo, Activity activity);

	Map<Date, List<EmployeeDailyActivities>> fetchEmployeeMonthlyAttendance(String empNo, Date startDate, Date endDate);

	List<EmployeeDailyActivities> fetchEmployeeTodayActivity(String empNo);

	Map<Date, List<EmployeeDailyActivities>> findEmployeeAllMonthlyActivities(String empNo, Date startDate,
			Date endDate);

	Boolean attendCustomer(String empNo, Activity activity, String custUsername);

	Boolean employeeFutureActivity(Map<Date, List<String>> employeeActivityMap, Activity activity);

}
