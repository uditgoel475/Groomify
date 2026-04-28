package com.uditgoel.groomify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record JwtJsonSubjectKey(Long id, String username, String email, UserType userType) {

	public static JwtJsonSubjectKey forSignIn(String username, UserType userType) {
		return new JwtJsonSubjectKey(null, username, null, userType);
	}
}
