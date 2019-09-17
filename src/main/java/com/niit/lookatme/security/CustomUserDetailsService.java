package com.niit.lookatme.security;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.lookatme.dto.JwtJsonSubjectKey;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	@Resource
	private ObjectMapper objectMapper;

	@Resource
	private ConfigurableApplicationContext appCtx;

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) {
		try {
			JwtJsonSubjectKey jwtJsonSubjectKey = objectMapper.readValue(username, JwtJsonSubjectKey.class);
			return ((UserDetailsService) appCtx
					.getBean(jwtJsonSubjectKey.getUserType().name().toLowerCase().concat("DetailsService")))
							.loadUserByUsername(jwtJsonSubjectKey.getUsername());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	public UserDetails loadUserById(JwtJsonSubjectKey jwtJsonSubjectKey) {
		return ((UserDetailsService) appCtx
				.getBean(jwtJsonSubjectKey.getUserType().name().toLowerCase().concat("DetailsService")))
						.loadUserByUsername(jwtJsonSubjectKey.getUsername());
	}

}
