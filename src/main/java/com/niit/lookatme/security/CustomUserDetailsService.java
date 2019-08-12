package com.niit.lookatme.security;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.lookatme.dto.JwtJsonSubjectKey;
import com.niit.lookatme.dto.UserType;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
	
	@Resource
	private CustomEmployeeDetailsService customEmployeeDetailsService;
	
	@Resource
	private CustomCustomerDetailsService customCustomerDetailsService;
	
	@Resource
	private ObjectMapper objectMapper;
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			JwtJsonSubjectKey jwtJsonSubjectKey = objectMapper.readValue(username, JwtJsonSubjectKey.class);
			if(jwtJsonSubjectKey.getUserType() == UserType.CUSTOMER) {
				return customCustomerDetailsService.loadUserByUsername(jwtJsonSubjectKey.getUsername());
			} else if(jwtJsonSubjectKey.getUserType() == UserType.EMPLOYEE) {
				return customEmployeeDetailsService.loadUserByUsername(jwtJsonSubjectKey.getUsername());
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		return null;
	}
	
	public UserDetails loadUserById(JwtJsonSubjectKey jwtJsonSubjectKey) {
		if(jwtJsonSubjectKey.getUserType() == UserType.CUSTOMER) {
			return customCustomerDetailsService.loadUserByUsername(jwtJsonSubjectKey.getUsername());
		} else if(jwtJsonSubjectKey.getUserType() == UserType.EMPLOYEE) {
			return customEmployeeDetailsService.loadUserByUsername(jwtJsonSubjectKey.getUsername());
		}
		
		return null;
	}

}
