package com.niit.lookatme.employee.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomerJobsInput {

	private String subJobId;
	private String employeeUsername;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private Date date;

	public CustomerJobsInput(String subJobId, String employeeUsername, Date date) {
		super();
		this.subJobId = subJobId;
		this.employeeUsername = employeeUsername;
		this.date = date;
	}

	/**
	 * @return the subJobId
	 */
	public String getSubJobId() {
		return subJobId;
	}

	/**
	 * @return the employeeUsername
	 */
	public String getEmployeeUsername() {
		return employeeUsername;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param subJobId
	 *            the subJobId to set
	 */
	public void setSubJobId(String subJobId) {
		this.subJobId = subJobId;
	}

	/**
	 * @param employeeUsername
	 *            the employeeUsername to set
	 */
	public void setEmployeeUsername(String employeeUsername) {
		this.employeeUsername = employeeUsername;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
}
