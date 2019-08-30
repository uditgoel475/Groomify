package com.niit.lookatme.controller;

import java.net.URI;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.lookatme.dto.JwtAuthenticationResponse;
import com.niit.lookatme.dto.JwtJsonSubjectKey;
import com.niit.lookatme.dto.LoginRequest;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.dto.customer.CustomerDTO;
import com.niit.lookatme.exception.AlreadyExistsException;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.security.JwtTokenProvider;

@RestController
@RequestMapping("api/auth/customer")
public class CustomerAuthController {

	@Resource
	private AuthenticationManager authenticationManager;

	@Resource
	private JwtTokenProvider tokenProvider;

	@Resource(name = "customerFacade")
	private CustomerFacade customerFacade;

	@Resource
	private ObjectMapper objectMapper;

	@PostMapping("signin")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws JsonProcessingException {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				objectMapper.writeValueAsString(new JwtJsonSubjectKey(loginRequest.getUsername(), UserType.CUSTOMER)),
				loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication, UserType.CUSTOMER);
		return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
	}

	@PostMapping("signup")
	public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerInput) {
		if (!customerFacade.checkUsernameAvailability(customerInput.getUsername())) {
			throw new AlreadyExistsException("Username", customerInput.getUsername());
		}

		if (!customerFacade.checkEmailAvailability(customerInput.getEmail())) {
			throw new AlreadyExistsException("Email", customerInput.getEmail());
		}

		String username = customerFacade.createNewCustomer(customerInput);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/customer/{username}")
				.buildAndExpand(username).toUri();
		return ResponseEntity.created(location).body("Customer Created Successfully");
	}

}
