package com.uditgoel.groomify.utils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:employeeroster.properties")
@ConfigurationProperties("roster")
public class LoadEmployeeRosterTableConfig {

	private List<Roster> rosters = new ArrayList<>();

	public static class Roster {
		private DayOfWeek shiftStartDay;
		private DayOfWeek shiftEndDay;
		// '08:00:00'
		private LocalTime genericInTime;
		private LocalTime totalShiftTime;

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

		/**
		 * @return the genericInTime
		 */
		public LocalTime getGenericInTime() {
			return genericInTime;
		}

		/**
		 * @return the totalShiftTime
		 */
		public LocalTime getTotalShiftTime() {
			return totalShiftTime;
		}

		/**
		 * @param shiftStartDay
		 *                      the shiftStartDay to set
		 */
		public void setShiftStartDay(DayOfWeek shiftStartDay) {
			this.shiftStartDay = shiftStartDay;
		}

		/**
		 * @param shiftEndDay
		 *                    the shiftEndDay to set
		 */
		public void setShiftEndDay(DayOfWeek shiftEndDay) {
			this.shiftEndDay = shiftEndDay;
		}

		/**
		 * @param genericInTime
		 *                      the genericInTime to set
		 */
		public void setGenericInTime(LocalTime genericInTime) {
			this.genericInTime = genericInTime;
		}

		/**
		 * @param totalShiftTime
		 *                       the totalShiftTime to set
		 */
		public void setTotalShiftTime(LocalTime totalShiftTime) {
			this.totalShiftTime = totalShiftTime;
		}

	}

	/**
	 * @return the rosters
	 */
	public List<Roster> getRosters() {
		return rosters;
	}

	/**
	 * @param rosters
	 *                the rosters to set
	 */
	public void setRosters(List<Roster> rosters) {
		this.rosters = rosters;
	}

}