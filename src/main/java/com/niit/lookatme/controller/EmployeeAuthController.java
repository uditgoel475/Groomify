package com.niit.lookatme.controller;

import java.net.URI;
import java.util.Map;

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
import com.niit.lookatme.dto.employee.EmployeeInput;
import com.niit.lookatme.exception.AlreadyExistsException;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.security.JwtTokenProvider;

@RestController
@RequestMapping("api/auth/employee")
public class EmployeeAuthController {

	@Resource
	private AuthenticationManager authenticationManager;

	@Resource
	private JwtTokenProvider tokenProvider;

	@Resource(name = "employeeFacade")
	private EmployeeFacade employeeFacade;

	@Resource
	private ObjectMapper objectMapper;

	@PostMapping("refreshToken")
	public ResponseEntity<JwtAuthenticationResponse> regenerateAccessToken(
			@RequestBody Map<String, String> refreshTokenMap) throws JsonProcessingException {
		String refreshToken = refreshTokenMap.get("refreshToken");
		JwtJsonSubjectKey jwtJsonSubjectKey = tokenProvider.getJwtJsonSubjectKeyFromRefreshToken(refreshToken);
		Authentication authentication = authenticationManager
				.authenticate(
						new UsernamePasswordAuthenticationToken(
								objectMapper.writeValueAsString(
										new JwtJsonSubjectKey(jwtJsonSubjectKey.getUsername(), UserType.EMPLOYEE)),
								null));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		JwtAuthenticationResponse jwtResponseToken = tokenProvider.createAccessToken(jwtJsonSubjectKey);
		jwtResponseToken.setRefreshToken(refreshToken);
		return ResponseEntity.ok(jwtResponseToken);
	}

	@PostMapping("signin")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws JsonProcessingException {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				objectMapper.writeValueAsString(new JwtJsonSubjectKey(loginRequest.getUsername(), UserType.EMPLOYEE)),
				loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return ResponseEntity.ok(tokenProvider.generateToken(authentication, UserType.EMPLOYEE));
	}

	@PostMapping("signup")
	public ResponseEntity<String> createEmployee(@RequestBody EmployeeInput employeeInput) {
		if (!employeeFacade.checkUsernameAvailability(employeeInput.getUsername())) {
			throw new AlreadyExistsException("Username", employeeInput.getUsername());
		}
		String username = employeeFacade.createNewEmployee(employeeInput);
		URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("api/employee/{username}")
				.buildAndExpand(username).toUri();
		return ResponseEntity.created(location).body("Employee Created Successfully");
	}

}
