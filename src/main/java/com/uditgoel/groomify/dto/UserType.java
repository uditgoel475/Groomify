package com.uditgoel.groomify.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {

	EMPLOYEE("EMP"),
	CUSTOMER("CUS");
	
	private String value;

	UserType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static UserType fromValue(String text) {
		for (UserType b : UserType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
