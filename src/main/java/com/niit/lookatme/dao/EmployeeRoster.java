package com.niit.lookatme.dao;

import java.io.Serializable;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "SCHEDULE")
public class EmployeeRoster implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2709543340266164869L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SCHEDULE_ID")
	private Long id;

	@Column(name = "IN_TIME", nullable = false)
	private Time inTime;

	@Column(name = "OUT_TIME", nullable = false)
	private Time outTime;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "WEEK_START_DAY")
	@ColumnDefault("'MONDAY'")
	private DayOfWeek weekStartDay;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "WEEK_END_DAY")
	@ColumnDefault("'SUNDAY'")
	private DayOfWeek weekEndDay;

	@Column(name = "SHIFT_STATUS")
	@ColumnDefault("true")
	private Boolean shiftStatus;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the inTime
	 */
	public Date getInTime() {
		return inTime;
	}

	/**
	 * @return the outTime
	 */
	public Date getOutTime() {
		return outTime;
	}

	/**
	 * @return the weekStartDay
	 */
	public DayOfWeek getWeekStartDay() {
		return weekStartDay;
	}

	/**
	 * @return the weekEndDay
	 */
	public DayOfWeek getWeekEndDay() {
		return weekEndDay;
	}

	/**
	 * @return the shiftStatus
	 */
	public Boolean isShiftStatus() {
		return shiftStatus;
	}

	/**
	 * @param weekStartDay
	 *            the weekStartDay to set
	 */
	public void setWeekStartDay(DayOfWeek weekStartDay) {
		this.weekStartDay = weekStartDay;
	}

	/**
	 * @param weekEndDay
	 *            the weekEndDay to set
	 */
	public void setWeekEndDay(DayOfWeek weekEndDay) {
		this.weekEndDay = weekEndDay;
	}

	/**
	 * @param shiftStatus
	 *            the shiftStatus to set
	 */
	public void setShiftStatus(Boolean shiftStatus) {
		this.shiftStatus = shiftStatus;
	}

	/**
	 * @param inTime
	 *            the inTime to set
	 */
	public void setInTime(Time inTime) {
		this.inTime = inTime;
	}

	/**
	 * @param outTime
	 *            the outTime to set
	 */
	public void setOutTime(Time outTime) {
		this.outTime = outTime;
	}

}
