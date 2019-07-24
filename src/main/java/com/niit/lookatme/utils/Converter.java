package com.niit.lookatme.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author Konika
 *
 */
public class Converter {

	public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static final Gson gson = new Gson();

	private Converter() {
		/**
		 * Default Constructor
		 */
	}

	public static String object2Json(Object object) {
		try {
			OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return OBJECT_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException ex) {
				return new GsonBuilder().create().toJson(object);
		}
	}
	

}
