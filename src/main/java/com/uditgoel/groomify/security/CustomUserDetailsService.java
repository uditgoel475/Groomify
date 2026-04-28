package com.uditgoel.groomify.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uditgoel.groomify.dto.JwtJsonSubjectKey;
import com.uditgoel.groomify.dto.UserType;

/**
 * Dispatches to the customer- or employee-specific UserDetailsService based on the
 * {@link UserType} embedded in the JSON-serialized username.
 *
 * <p>The "username" arriving from {@code AuthenticationManager.authenticate(...)} is actually the
 * JSON-serialized {@link JwtJsonSubjectKey} produced by the auth controllers, so we deserialize
 * to discover which downstream service to delegate to.
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private final ObjectMapper objectMapper;
	private final Map<UserType, UserDetailsService> delegates;

	public CustomUserDetailsService(ObjectMapper objectMapper,
			@Qualifier("customerDetailsService") UserDetailsService customerDetailsService,
			@Qualifier("employeeDetailsService") UserDetailsService employeeDetailsService) {
		this.objectMapper = objectMapper;
		this.delegates = Map.of(
				UserType.CUSTOMER, customerDetailsService,
				UserType.EMPLOYEE, employeeDetailsService);
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		JwtJsonSubjectKey jwtJsonSubjectKey;
		try {
			jwtJsonSubjectKey = objectMapper.readValue(username, JwtJsonSubjectKey.class);
		} catch (IOException e) {
			throw new UsernameNotFoundException("Could not parse username payload", e);
		}
		return loadUserById(jwtJsonSubjectKey);
	}

	public UserDetails loadUserById(JwtJsonSubjectKey jwtJsonSubjectKey) {
		UserDetailsService delegate = delegates.get(jwtJsonSubjectKey.userType());
		if (delegate == null) {
			throw new UsernameNotFoundException("Unknown user type: " + jwtJsonSubjectKey.userType());
		}
		return delegate.loadUserByUsername(jwtJsonSubjectKey.username());
	}

}
