package com.niit.lookatme.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

	public AppUtils() {
	}

	@Value("${customer.password.expire}")
	private static boolean isPasswordExpire;

	@Value("${customer.password.expiry.policy.days}")
	private static int addExpiryDays;

	public static Date getCustomerExpirationDateFromCurrent(Calendar cal) {
		if (isPasswordExpire) {
			cal.add(Calendar.DATE, addExpiryDays);
			return cal.getTime();
		}
		return null;
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
}
