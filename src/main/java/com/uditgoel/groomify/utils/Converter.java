package com.uditgoel.groomify.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class Converter {

	private static final ObjectMapper LOG_MAPPER = new ObjectMapper()
			.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
			.findAndRegisterModules();

	private Converter() {
	}

	public static String object2Json(Object object) {
		try {
			return LOG_MAPPER.writeValueAsString(object);
		} catch (JsonProcessingException ex) {
			return "<unserializable: " + ex.getOriginalMessage() + ">";
		}
	}

	public static Date convertDateToStartOfDay(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date convertLocalDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date localTimeToDate(LocalTime localTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(0, 0, 0, localTime.getHour(), localTime.getMinute(), localTime.getSecond());
		return calendar.getTime();
	}

	public static LocalTime dateToLocalTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
	}

}
