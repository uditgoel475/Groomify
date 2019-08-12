package com.niit.lookatme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7315966023998444985L;

	public AlreadyExistsException(String type, String value) {
		super(String.format("%s '%s' already exists.", type, value));
	}
	
	public AlreadyExistsException(String msg) {
		super(msg);
	}

}
