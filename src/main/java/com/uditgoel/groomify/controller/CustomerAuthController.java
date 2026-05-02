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
import com.uditgoel.groomify.dto.customer.CustomerDTO;
import com.uditgoel.groomify.exception.AlreadyExistsException;
import com.uditgoel.groomify.facade.CustomerFacade;
import com.uditgoel.groomify.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth/customer")
public class CustomerAuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenProvider tokenProvider;

	private final CustomerFacade customerFacade;

	private final ObjectMapper objectMapper;

	public CustomerAuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
			@Qualifier("customerFacade") CustomerFacade customerFacade, ObjectMapper objectMapper) {
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
		this.customerFacade = customerFacade;
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
				objectMapper.writeValueAsString(JwtJsonSubjectKey.forSignIn(loginRequest.username(), UserType.CUSTOMER)),
				loginRequest.password()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return ResponseEntity.ok(tokenProvider.generateToken(authentication, UserType.CUSTOMER));
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
