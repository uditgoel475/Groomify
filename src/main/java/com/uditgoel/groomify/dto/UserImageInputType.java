package com.uditgoel.groomify.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserImageInputType {

	PROFILE("profile"),
	GOVTID("govtId");
	
	private String value;

	UserImageInputType(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static UserImageInputType fromValue(String text) {
		for (UserImageInputType b : UserImageInputType.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}

}
