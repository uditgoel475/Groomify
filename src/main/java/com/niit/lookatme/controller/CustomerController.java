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

import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dto.CustomerInput;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@RestController
@RequestMapping("api/employee")
public class CustomerController {

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;
	
	@PostMapping("upload/profile/{custNo}")
	public ResponseEntity<Boolean> uploadCustomerImageProfile(@RequestParam("file") MultipartFile file,
			@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, custNo, UserImageInputType.PROFILE)));
	}

	@PostMapping("upload/govt/{custNo}")
	public ResponseEntity<Boolean> uploadCustomerImageGovt(@RequestParam("file") MultipartFile file,
			@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, custNo, UserImageInputType.GOVTID)));
	}

	@PostMapping("create")
	public ResponseEntity<String> createCustomer(@RequestBody CustomerInput customerInput) {
		return ResponseEntity.ok(customerFacade.createNewCustomer(customerInput));
	}

	@PostMapping("changepwd/{custNo}")
	public ResponseEntity<Boolean> updateCustomerPassword(@PathVariable("custNo") String custNo,
			@RequestBody Map<String, String> encryptedPassword) {
		return ResponseEntity.ok(customerFacade.changeCustomerPassword(custNo, encryptedPassword.get("custPass")));
	}
	
}
