package com.uditgoel.groomify.dao.employee;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

	@Temporal(TemporalType.TIME)
	@Column(name = "IN_TIME", nullable = false)
	private Date inTime;

	@Temporal(TemporalType.TIME)
	@Column(name = "OUT_TIME", nullable = false)
	private Date outTime;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "WEEK_START_DAY")
	@ColumnDefault("'MONDAY'")
	private DayOfWeek weekStartDay;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "WEEK_END_DAY")
	@ColumnDefault("'SUNDAY'")
	private DayOfWeek weekEndDay;

	@Column(name = "IS_SHIFT_ACTIVE")
	@ColumnDefault("true")
	private Boolean isShiftActive;
	
	@OneToMany(mappedBy = "schedule", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
	private List<Employee> employees;

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
	 * @param inTime
	 *            the inTime to set
	 */
	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	/**
	 * @param outTime
	 *            the outTime to set
	 */
	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	/**
	 * @return the isShiftActive
	 */
	public Boolean getIsShiftActive() {
		return isShiftActive;
	}

	/**
	 * @return the employees
	 */
	public List<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param isShiftActive the isShiftActive to set
	 */
	public void setIsShiftActive(Boolean isShiftActive) {
		this.isShiftActive = isShiftActive;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

}
