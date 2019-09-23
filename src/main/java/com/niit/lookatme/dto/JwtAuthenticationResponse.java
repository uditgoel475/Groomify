package com.niit.lookatme.dto;

import java.util.Date;

/**
 * @author Konika
 */
public class JwtAuthenticationResponse {
	private String accessToken;
	private String refreshToken;
	private Date expiresAt;

	public JwtAuthenticationResponse(String accessToken, String refreshToken, Date expiresAt) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.expiresAt = expiresAt;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Date getExpiresAt() {
		return expiresAt;
	}

}
