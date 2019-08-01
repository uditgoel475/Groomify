package com.niit.lookatme.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobStatus {

	ENQUIRY("ENQUIRY"),
	PENDING("PENDING"),
	INPROGRESS("INPROGRESS"),
	COMPLETED("COMPLETED"),
	CANCELLED("CANCELLED");
	
	private String value;

	JobStatus(String value) {
		this.value = value;
	}

	@Override
	@JsonValue
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static JobStatus fromValue(String text) {
		for (JobStatus b : JobStatus.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
