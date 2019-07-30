package com.niit.lookatme.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {
	
	private AppUtils() {}

	@Value("${customer.password.expire}")
	private static boolean isPasswordExpire;

	@Value("${customer.password.expiry.policy.days}")
	private static int addExpiryDays;

	public static Date getCustomerExpirationDateFromCurrent(Calendar cal) {
		if(isPasswordExpire) {
			cal.add(Calendar.DATE, addExpiryDays);
			return cal.getTime();
		}
		return null;
	}
	
	
}
