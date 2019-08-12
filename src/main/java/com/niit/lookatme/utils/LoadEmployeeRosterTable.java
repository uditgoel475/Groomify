package com.niit.lookatme.utils;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.niit.lookatme.dao.employee.EmployeeRoster;
import com.niit.lookatme.dao.repository.EmployeeRosterRepository;

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
				EmployeeRoster empRster = new EmployeeRoster();
				empRster.setIsShiftActive(true);
				empRster.setWeekStartDay(roster.getShiftStartDay());
				empRster.setWeekEndDay(roster.getShiftEndDay());
				empRster.setInTime(roster.getGenericInTime());
				empRster.setOutTime(roster.getGenericInTime()
						.plusHours(roster.getTotalShiftTime().getHour())
						.plusMinutes(roster.getTotalShiftTime().getMinute())
						.plusSeconds(roster.getTotalShiftTime().getSecond()));
				empRosters.add(empRster);
			});
			employeeRosterRepository.saveAll(empRosters);
		}
	}
}
