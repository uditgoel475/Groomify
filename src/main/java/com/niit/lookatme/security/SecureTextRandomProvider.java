package com.niit.lookatme.security;

import java.security.SecureRandom;

import org.apache.commons.text.TextRandomProvider;

public class SecureTextRandomProvider implements TextRandomProvider {

	private static final SecureRandom secRan = new SecureRandom();
	
	@Override
	public int nextInt(int max) {
		return secRan.nextInt(max);
	}

}
