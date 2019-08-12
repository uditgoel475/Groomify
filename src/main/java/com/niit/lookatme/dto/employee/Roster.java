package com.niit.lookatme.dto.employee;

import java.time.DayOfWeek;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Roster {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime genericInTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
	private LocalTime genericOutTime;
	private DayOfWeek shiftStartDay;
	private DayOfWeek shiftEndDay;

	public Roster() {
		super();
	}
	
	public Roster(LocalTime genericInTime, LocalTime genericOutTime, DayOfWeek shiftStartDay, DayOfWeek shiftEndDay) {
		super();
		this.genericInTime = genericInTime;
		this.genericOutTime = genericOutTime;
		this.shiftStartDay = shiftStartDay;
		this.shiftEndDay = shiftEndDay;
	}

	/**
	 * @return the genericInTime
	 */
	public LocalTime getGenericInTime() {
		return genericInTime;
	}

	/**
	 * @return the genericOutTime
	 */
	public LocalTime getGenericOutTime() {
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
