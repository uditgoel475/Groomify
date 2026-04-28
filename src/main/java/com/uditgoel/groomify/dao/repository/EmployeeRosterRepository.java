package com.uditgoel.groomify.dao.repository;

import java.time.DayOfWeek;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.employee.EmployeeRoster;

@Repository("employeeRosterRepository")
public interface EmployeeRosterRepository extends JpaRepository<EmployeeRoster, Long> {

	@Query("select e from EmployeeRoster e where e.inTime = :inTime and e.outTime = :outTime "
			+ "and e.weekStartDay = :weekStartDay and e.weekEndDay = :weekEndDay and e.isShiftActive = true")
	Optional<EmployeeRoster> findActiveRosterByGivenInput(@Param("inTime") Date inTime,
			@Param("outTime") Date outTime, @Param("weekStartDay") DayOfWeek weekStartDay,
			@Param("weekEndDay") DayOfWeek weekEndDay);
}
