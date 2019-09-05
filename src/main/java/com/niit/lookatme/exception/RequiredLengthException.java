package com.niit.lookatme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LENGTH_REQUIRED)
public class RequiredLengthException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4425157001883535485L;

	public RequiredLengthException(String field, int inputLengthRequired, String fieldVal) {
		super(String.format("Input %s '%s' does not match the minimum required length %s", field, fieldVal,
				inputLengthRequired));
	}

	public RequiredLengthException(String msg) {
		super(msg);
	}

}
