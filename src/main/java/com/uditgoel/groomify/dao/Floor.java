package com.uditgoel.groomify.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Floor {

	BASEMENT4("FB4"),
	BASEMENT3("FB3"),
	BASEMENT2("FB2"),
	BASEMENT1("FB1"),
	GROUND("F0"),
	FIRST("F1"),
	SECOND("F2"),
	THIRD("F3"),
	FOURTH("F4"),
	FIFTH("F5"),
	SIXTH("F6"),
	SEVENTH("F7"),
	EIGHT("F8");
	
	private String value;

	Floor(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
	
	@JsonValue	
	public String enumName() {
		return name();
	}

	@JsonCreator
	public static Floor fromValue(String text) {
		for (Floor b : Floor.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
