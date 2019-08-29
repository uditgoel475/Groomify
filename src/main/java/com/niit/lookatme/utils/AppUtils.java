package com.niit.lookatme.utils;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppUtils {

	public AppUtils() {
		super();
	}
	
	private static boolean isCustomerPasswordExpire;
	private static int addCustomerPasswordExpiryDays;
	private static boolean isEmployeePasswordExpire;
	private static int addEmployeePasswordExpiryDays;
	
	@Value("${customer.password.expire.policy}")
	public void setCustomerPasswordExpire(boolean isCustomerPasswordExpire) {
		AppUtils.isCustomerPasswordExpire = isCustomerPasswordExpire;
	}

	@Value("${customer.password.expiry.policy.days}")
	public void setAddCustomerPasswordExpiryDays(int addCustomerPasswordExpiryDays) {
		AppUtils.addCustomerPasswordExpiryDays = addCustomerPasswordExpiryDays;
	}

	@Value("${employee.password.expire.policy}")
	public void setEmployeePasswordExpire(boolean isEmployeePasswordExpire) {
		AppUtils.isEmployeePasswordExpire = isEmployeePasswordExpire;
	}

	@Value("${employee.password.expiry.policy.days}")
	public void setAddEmployeePasswordExpiryDays(int addEmployeePasswordExpiryDays) {
		AppUtils.addEmployeePasswordExpiryDays = addEmployeePasswordExpiryDays;
	}

	public static Date getEmployeeExpirationDateFromCurrent(Calendar cal) {
		if (isEmployeePasswordExpire) {
			cal.add(Calendar.DATE, addEmployeePasswordExpiryDays);
			return cal.getTime();
		}
		return null;
	}

	public static Date getCustomerExpirationDateFromCurrent(Calendar cal) {
		if (isCustomerPasswordExpire) {
			cal.add(Calendar.DATE, addCustomerPasswordExpiryDays);
			return cal.getTime();
		}
		return null;
	}

}
