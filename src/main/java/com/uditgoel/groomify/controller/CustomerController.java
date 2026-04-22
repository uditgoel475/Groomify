package com.uditgoel.groomify.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uditgoel.groomify.dto.UserImageInputType;
import com.uditgoel.groomify.dto.UserType;
import com.uditgoel.groomify.dto.customer.CustomerOutDTO;
import com.uditgoel.groomify.facade.CustomerFacade;
import com.uditgoel.groomify.utils.CustomerAndEmployeeUtils;

@RestController
@RequestMapping("api/customer")
public class CustomerController {

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@PostMapping("upload/profile/{custNo}")
	public ResponseEntity<Boolean> uploadCustomerImageProfile(@RequestBody MultipartFile file,
			@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, custNo, UserImageInputType.PROFILE)));
	}

	@PostMapping("upload/govt/{custNo}")
	public ResponseEntity<Boolean> uploadCustomerImageGovt(@RequestBody MultipartFile file,
			@PathVariable("custNo") String custNo) {
		return ResponseEntity.ok(!StringUtils.isEmpty(CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
				file, custNo, UserImageInputType.GOVTID)));
	}

	@GetMapping("{username}")
	public ResponseEntity<CustomerOutDTO> getCustomerDTO(@PathVariable("username") String username) {
		return ResponseEntity.ok(customerFacade.fetchCustomerDTO(username));
	}

	@PostMapping("changepwd/{custNo}")
	public ResponseEntity<Boolean> updateCustomerPassword(@PathVariable("custNo") String custNo,
			@RequestBody Map<String, String> psswrds) {
		return ResponseEntity.ok(customerFacade.changeCustomerPassword(custNo, psswrds.get("currentPassword"), psswrds.get("newPassword")));
	}
	
	@GetMapping("checkEmailAvailability/{email:.+}")
	public ResponseEntity<Boolean> checkEmailAvailability(@PathVariable("email") String email) {
		return ResponseEntity.ok(customerFacade.checkEmailAvailability(email));
	}
	
	@GetMapping("checkUsernameAvailability/{username:.+}")
	public ResponseEntity<Boolean> checkUsernameAvailability(@PathVariable("username") String username) {
		return ResponseEntity.ok(customerFacade.checkUsernameAvailability(username));
	}
	
	@GetMapping("matchingNames")
	public ResponseEntity<List<CustomerOutDTO>> findAllMatchingName(@RequestHeader("name") String name) {
		return ResponseEntity.ok(customerFacade.findAllMatchingName(name));
	}
}