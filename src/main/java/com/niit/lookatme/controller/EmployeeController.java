package com.niit.lookatme.controller;

import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.dao.Activity;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.dto.employee.EmployeeActivityOut;
import com.niit.lookatme.dto.employee.EmployeeDTO;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

	@Resource(name = "employeeFacade")
	private EmployeeFacade employeeFacade;
	
	@GetMapping("{username}")
	public ResponseEntity<EmployeeDTO> getEmployeeDTO(@PathVariable("username") String username) {
		return ResponseEntity.ok(employeeFacade.fetchEmployeeDTO(username));
	}

	@PostMapping("upload/profile/{empNo}")
	public ResponseEntity<Boolean> uploadEmployeeImageProfile(@RequestParam("file") MultipartFile file,
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.EMPLOYEE,
				file, empNo, UserImageInputType.PROFILE)));
	}

	@PostMapping("upload/govt/{empNo}")
	public ResponseEntity<Boolean> uploadEmployeeImageGovt(@RequestParam("file") MultipartFile file,
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.EMPLOYEE,
				file, empNo, UserImageInputType.GOVTID)));
	}

	@PostMapping("changepwd/{empNo}")
	public ResponseEntity<Boolean> updateEmployeePassword(@PathVariable("empNo") String empNo,
			@RequestBody Map<String, String> encryptedPassword) {
		return ResponseEntity.ok(employeeFacade.changeEmployeePassword(empNo, encryptedPassword.get("empPass")));
	}

	@PostMapping("dailyactivity/{empNo}")
	public ResponseEntity<Boolean> employeeActivity(@PathVariable("empNo") String empNo,
			@RequestBody Map<String, String> activity) {
		if (StringUtils.isEmpty(activity.get("custuser")))
			return ResponseEntity.ok(employeeFacade.markActivity(empNo, Activity.fromValue(activity.get("activity"))));
		return ResponseEntity.ok(employeeFacade.attendCustomer(empNo, Activity.fromValue(activity.get("activity")),
				activity.get("custuser")));
	}

	@PostMapping("vacation/new")
	public ResponseEntity<Boolean> markEmployeeVacation(@RequestBody Map<Date, List<String>> employeeVacationMap) {
		return ResponseEntity.ok(employeeFacade.employeeFutureActivity(employeeVacationMap, Activity.VACATION));
	}

	@GetMapping("attendance/{empNo}/{month}/{year}")
	public ResponseEntity<Map<Date, List<EmployeeActivityOut>>> fetchEmployeeMonthlyAttendance(
			@PathVariable("empNo") String empNo, @PathVariable("year") Year year, @PathVariable("month") Month month) {
		return ResponseEntity.ok(employeeFacade.fetchEmployeeMonthlyAttendance(empNo, month, year.getValue()));
	}

	@GetMapping("dailyactivity/{empNo}")
	public ResponseEntity<List<EmployeeActivityOut>> fetchEmployeeTodayActivity(
			@PathVariable("empNo") String empNo) {
		return ResponseEntity.ok(employeeFacade.fetchEmployeeTodayActivity(empNo));
	}

	@GetMapping("activities/{empNo}/{month}/{year}")
	public ResponseEntity<Map<Date, List<EmployeeActivityOut>>> findEmployeeAllMonthlyActivities(
			@PathVariable("empNo") String empNo, @PathVariable("year") Year year, @PathVariable("month") Month month) {
		return ResponseEntity.ok(employeeFacade.findEmployeeAllMonthlyActivities(empNo, month, year.getValue()));
	}

	@GetMapping("service/getemployee/{service}")
	public ResponseEntity<List<EmployeeActivityOut>> fetchAllAvailableEmployeesMatchingSkills(
			@PathVariable("service") String service) {
		return ResponseEntity.ok(employeeFacade.fetchAllAvailableEmployeesMatchingSkills(service));
	}
}
