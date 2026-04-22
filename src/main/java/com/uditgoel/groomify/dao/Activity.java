package com.uditgoel.groomify.dao;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Activity {

	SALON_IN("in"),
	SALON_OUT("out"),
	LUNCH("lunch"),
	LUNCH_OVER("lunch_over"),
	ATTEND_CUSTOMER("attend_cust"),
	LEAVE_CUSTOMER("leave_cust"),
	VACATION("vacation"),
	MISCELLANEOUS("misc");
	
	private String value;

	Activity(String value) {
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
	public static Activity fromValue(String text) {
		for (Activity b : Activity.values()) {
			if (String.valueOf(b.value).equals(text)) {
				return b;
			}
		}
		return null;
	}
}
