package com.uditgoel.groomify.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.uditgoel.groomify.dao.employee.EmployeeRoster;
import com.uditgoel.groomify.dao.repository.EmployeeRosterRepository;

@Component
public class LoadEmployeeRosterTable {

	@Resource
	private LoadEmployeeRosterTableConfig loadEmployeeRosterTableConfig;

	@Resource
	private EmployeeRosterRepository employeeRosterRepository;

	@PostConstruct
	public void init() {
		if (employeeRosterRepository.count() == 0) {
			List<EmployeeRoster> empRosters = new ArrayList<>();

			loadEmployeeRosterTableConfig.getRosters().forEach(roster -> {

				Date genericInTime = Converter.localTimeToDate(roster.getGenericInTime());
				Date genericOutTime = Converter
						.localTimeToDate(roster.getGenericInTime().plusHours(roster.getTotalShiftTime().getHour())
								.plusMinutes(roster.getTotalShiftTime().getMinute())
								.plusSeconds(roster.getTotalShiftTime().getSecond()));

				EmployeeRoster empRster = new EmployeeRoster();
				empRster.setIsShiftActive(true);
				empRster.setWeekStartDay(roster.getShiftStartDay());
				empRster.setWeekEndDay(roster.getShiftEndDay());
				empRster.setInTime(genericInTime);
				empRster.setOutTime(genericOutTime);
				empRosters.add(empRster);
			});
			employeeRosterRepository.saveAll(empRosters);
		}
	}
}
