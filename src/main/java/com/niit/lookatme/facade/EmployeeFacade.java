package com.niit.lookatme.facade;

import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.niit.lookatme.dao.Activity;
import com.niit.lookatme.dto.employee.EmployeeActivityOut;
import com.niit.lookatme.dto.employee.EmployeeDTO;
import com.niit.lookatme.dto.employee.EmployeeInput;

public interface EmployeeFacade {

	List<EmployeeDTO> fetchAllExistingEmployeeCurrentWeekBirthdays();

	String createNewEmployee(EmployeeInput createEmployeeInput);

	Boolean changeEmployeePassword(String empNo, String encryptedPassword);

	Boolean markActivity(String empNo, Activity activity);

	Map<Date, List<EmployeeActivityOut>> fetchEmployeeMonthlyAttendance(String empNo, Month month, int year);

	List<EmployeeActivityOut> fetchEmployeeTodayActivity(String empNo);

	Map<Date, List<EmployeeActivityOut>> findEmployeeAllMonthlyActivities(String empNo, Month month, int year);

	Boolean attendCustomer(String empNo, Activity activity, String custUsername);

	Boolean employeeFutureActivity(Map<Date, List<String>> employeeActivityMap, Activity activity);

	List<EmployeeActivityOut> fetchAllAvailableEmployeesMatchingSkills(String service);

	EmployeeDTO fetchEmployeeDTO(String username);

}
