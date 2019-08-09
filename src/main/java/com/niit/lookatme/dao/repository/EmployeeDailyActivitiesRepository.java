package com.niit.lookatme.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.Activity;
import com.niit.lookatme.employee.dao.EmployeeDailyActivities;

@Repository("employeeDailyActivitiesRepository")
public interface EmployeeDailyActivitiesRepository extends CrudRepository<EmployeeDailyActivities, Long> {

	@Query("select e from EmployeeDailyActivities e LEFT JOIN FETCH e.employee t where "
			+ "e.time >= :startDate and "
			+ "e.time <= :endDate and "
			+ "e.activity in (:activities) and "
			+ "t.username = :username "
			+ "order by e.time")
	List<EmployeeDailyActivities> findEmployeeAttendance(@Param("activities") List<Activity> activities, @Param("username") String username, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	@Query("select e from EmployeeDailyActivities e LEFT JOIN FETCH e.employee t where "
			+ "EXTRACT (day from e.time) = day(current_date) and "
			+ "EXTRACT (month from e.time) = month(current_date) and "
			+ "EXTRACT (year from e.time) = year(current_date) and "
			+ "t.username = :username "
			+ "order by e.time")
	List<EmployeeDailyActivities> findEmployeeTodayActivities(@Param("username") String username);
	
	@Query("select e from EmployeeDailyActivities e LEFT JOIN FETCH e.employee t where "
			+ "e.time >= :startDate and "
			+ "e.time <= :endDate and "
			+ "t.username = :username "
			+ "order by e.time")
	List<EmployeeDailyActivities> findEmployeeAllMonthlyActivities(@Param("username") String username, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);
	
	@Query("select e from EmployeeDailyActivities e where "
			+ "EXTRACT (day from e.time) = day(current_date) and "
			+ "EXTRACT (month from e.time) = month(current_date) and "
			+ "EXTRACT (year from e.time) = year(current_date) "
			+ "order by e.employee, e.time")
	List<EmployeeDailyActivities> findAllEmployeesActivitiesToday();

}
