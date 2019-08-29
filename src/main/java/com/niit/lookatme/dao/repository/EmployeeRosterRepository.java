package com.niit.lookatme.dao.repository;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.employee.EmployeeRoster;

@Repository("employeeRosterRepository")
public interface EmployeeRosterRepository extends CrudRepository<EmployeeRoster, Long> {

	@Query("select e from EmployeeRoster e where inTime = :inTime and outTime = :outTime "
			+ "and weekStartDay = :weekStartDay and weekEndDay = :weekEndDay and isShiftActive = 1")
	Optional<EmployeeRoster> findActiveRosterByGivenInput(@Param("inTime") Date inTime,
			@Param("outTime") Date outTime, @Param("weekStartDay") DayOfWeek weekStartDay,
			@Param("weekEndDay") DayOfWeek weekEndDay);
}
