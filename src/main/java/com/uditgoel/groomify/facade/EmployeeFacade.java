package com.uditgoel.groomify.facade;

import java.time.Month;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.uditgoel.groomify.dao.Activity;
import com.uditgoel.groomify.dto.employee.EmployeeActivityOut;
import com.uditgoel.groomify.dto.employee.EmployeeDTO;
import com.uditgoel.groomify.dto.employee.EmployeeInput;

public interface EmployeeFacade {

	List<EmployeeDTO> fetchAllExistingEmployeeCurrentWeekBirthdays();

	String createNewEmployee(EmployeeInput createEmployeeInput);

	Boolean changeEmployeePassword(String empNo, String currentPass, String newPass);

	Boolean markActivity(String empNo, Activity activity);

	Map<Date, List<EmployeeActivityOut>> fetchEmployeeMonthlyAttendance(String empNo, Month month, int year);

	List<EmployeeActivityOut> fetchEmployeeTodayActivity(String empNo);

	Map<Date, List<EmployeeActivityOut>> findEmployeeAllMonthlyActivities(String empNo, Month month, int year);

	Boolean attendCustomer(String empNo, Activity activity, String custUsername);

	Boolean employeeFutureActivity(Map<Date, List<String>> employeeActivityMap, Activity activity);

	List<EmployeeActivityOut> fetchAllAvailableEmployeesMatchingSkills(String service);

	EmployeeDTO fetchEmployeeDTO(String username);

	Boolean checkUsernameAvailability(String username);

	List<EmployeeDTO> findAllMatchingName(String name);

}
