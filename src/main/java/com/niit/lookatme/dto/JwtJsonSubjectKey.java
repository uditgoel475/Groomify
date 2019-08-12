package com.niit.lookatme.dto;

public class JwtJsonSubjectKey {

	private Long id;
	private String username;
	private String email;
	private UserType userType;
	
	public JwtJsonSubjectKey() {
		super();
	}

	public JwtJsonSubjectKey(Long id, String username, String email, UserType userType) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.userType = userType;
	}
	
	public JwtJsonSubjectKey(String username, UserType userType) {
		super();
		this.username = username;
		this.userType = userType;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	public UserType getUserType() {
		return userType;
	}
}
