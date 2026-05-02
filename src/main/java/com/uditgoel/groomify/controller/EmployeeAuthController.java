package com.uditgoel.groomify.controller;

import java.net.URI;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
import com.uditgoel.groomify.dto.JwtAuthenticationResponse;
import com.uditgoel.groomify.dto.JwtJsonSubjectKey;
import com.uditgoel.groomify.dto.LoginRequest;
import com.uditgoel.groomify.dto.UserType;
import com.uditgoel.groomify.dto.employee.EmployeeInput;
import com.uditgoel.groomify.exception.AlreadyExistsException;
import com.uditgoel.groomify.facade.EmployeeFacade;
import com.uditgoel.groomify.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth/employee")
public class EmployeeAuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider tokenProvider;

	private final EmployeeFacade employeeFacade;

	private final ObjectMapper objectMapper;

	public EmployeeAuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
			@Qualifier("employeeFacade") EmployeeFacade employeeFacade, ObjectMapper objectMapper) {
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		this.employeeFacade = employeeFacade;
		this.objectMapper = objectMapper;
	}

	@PostMapping("refreshToken")
	public ResponseEntity<JwtAuthenticationResponse> regenerateAccessToken(
			@RequestBody Map<String, String> refreshTokenMap) throws JsonProcessingException {
		String refreshToken = refreshTokenMap.get("refreshToken");
		JwtTokenProvider.RotationResult rotation = tokenProvider.rotateRefreshToken(refreshToken)
				.orElseThrow(() -> new BadCredentialsException("Invalid or expired refresh token"));
		return ResponseEntity.ok(tokenProvider.createAccessToken(rotation.subject(), rotation.newRefreshToken()));
	}

	@PostMapping("signin")
	public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
			throws JsonProcessingException {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				objectMapper.writeValueAsString(JwtJsonSubjectKey.forSignIn(loginRequest.username(), UserType.EMPLOYEE)),
				loginRequest.password()));

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
