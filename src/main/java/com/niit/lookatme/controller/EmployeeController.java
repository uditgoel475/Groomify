package com.niit.lookatme.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
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
import com.niit.lookatme.employee.dto.EmployeeInput;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

	@Resource(name = "employeeFacade")
	private EmployeeFacade employeeFacade;
	
	@PostMapping("create")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeInput employeeInput) {
		return ResponseEntity.ok(employeeFacade.createNewEmployee(employeeInput));
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
	public ResponseEntity<Boolean> employeeActivity(@PathVariable("empNo") String empNo, @RequestBody Map<String, String> activity) {
		return ResponseEntity.ok(employeeFacade.markActivity(empNo, Activity.fromValue(activity.get("activity")), activity.get("custuser")));
	}
}
