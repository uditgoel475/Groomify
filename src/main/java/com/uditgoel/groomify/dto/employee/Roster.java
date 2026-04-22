package com.uditgoel.groomify.dto.employee;

import java.time.DayOfWeek;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Roster {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date genericInTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private Date genericOutTime;
	private DayOfWeek shiftStartDay;
	private DayOfWeek shiftEndDay;

	public Roster() {
		super();
	}
	
	public Roster(Date genericInTime, Date genericOutTime, DayOfWeek shiftStartDay, DayOfWeek shiftEndDay) {
		super();
		this.genericInTime = genericInTime;
		this.genericOutTime = genericOutTime;
		this.shiftStartDay = shiftStartDay;
		this.shiftEndDay = shiftEndDay;
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
	 * @return the shiftStartDay
	 */
	public DayOfWeek getShiftStartDay() {
		return shiftStartDay;
	}

	/**
	 * @return the shiftEndDay
	 */
	public DayOfWeek getShiftEndDay() {
		return shiftEndDay;
	}

}
