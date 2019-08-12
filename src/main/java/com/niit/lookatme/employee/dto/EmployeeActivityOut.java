package com.niit.lookatme.employee.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.niit.lookatme.dao.Activity;

public class EmployeeActivityOut {

	private String username;
	private String fName;
	private String mName;
	private String lName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date genericInTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date genericOutTime;
	private boolean availableOvertime;
	private long primaryContact;
	private long secondaryContact;
	private long whatsappContact;
	private Activity activity;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd'T'HH:mm:ss")
	private Date activityTime;
	
	private String customerUsername;
	private String customerName;

	public EmployeeActivityOut(String fName, Date genericInTime,
			Date genericOutTime, boolean availableOvertime, long primaryContact, Activity activity, Date activityTime) {
		super();
		this.fName = fName;
		this.genericInTime = genericInTime;
		this.genericOutTime = genericOutTime;
		this.availableOvertime = availableOvertime;
		this.primaryContact = primaryContact;
		this.activity = activity;
		this.activityTime = activityTime;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}
	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}
	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}
	/**
	 * @return the genericInTime
	 */
	public Date getGenericInTime() {
		return genericInTime;
	}
	/**
	 * @return the genericOutTime
	 */
	public Date getGenericOutTime() {
		return genericOutTime;
	}
	/**
	 * @return the availableOvertime
	 */
	public boolean isAvailableOvertime() {
		return availableOvertime;
	}
	/**
	 * @return the primaryContact
	 */
	public long getPrimaryContact() {
		return primaryContact;
	}
	/**
	 * @return the secondaryContact
	 */
	public long getSecondaryContact() {
		return secondaryContact;
	}
	/**
	 * @return the whatsappContact
	 */
	public long getWhatsappContact() {
		return whatsappContact;
	}
	/**
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}
	/**
	 * @return the activityTime
	 */
	public Date getActivityTime() {
		return activityTime;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}
	/**
	 * @param mName the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}
	/**
	 * @param lName the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}
	/**
	 * @param genericInTime the genericInTime to set
	 */
	public void setGenericInTime(Date genericInTime) {
		this.genericInTime = genericInTime;
	}
	/**
	 * @param genericOutTime the genericOutTime to set
	 */
	public void setGenericOutTime(Date genericOutTime) {
		this.genericOutTime = genericOutTime;
	}
	/**
	 * @param availableOvertime the availableOvertime to set
	 */
	public void setAvailableOvertime(boolean availableOvertime) {
		this.availableOvertime = availableOvertime;
	}
	/**
	 * @param primaryContact the primaryContact to set
	 */
	public void setPrimaryContact(long primaryContact) {
		this.primaryContact = primaryContact;
	}
	/**
	 * @param secondaryContact the secondaryContact to set
	 */
	public void setSecondaryContact(long secondaryContact) {
		this.secondaryContact = secondaryContact;
	}
	/**
	 * @param whatsappContact the whatsappContact to set
	 */
	public void setWhatsappContact(long whatsappContact) {
		this.whatsappContact = whatsappContact;
	}
	/**
	 * @param activity the activity to set
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	/**
	 * @param activityTime the activityTime to set
	 */
	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}
	/**
	 * @return the customerUsername
	 */
	public String getCustomerUsername() {
		return customerUsername;
	}
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}
	/**
	 * @param customerUsername the customerUsername to set
	 */
	public void setCustomerUsername(String customerUsername) {
		this.customerUsername = customerUsername;
	}
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}


}
