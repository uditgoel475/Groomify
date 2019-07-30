package com.niit.lookatme.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.employee.dao.EmployeeDailyActivities;

@Repository("employeeDailyActivitiesRepository")
public interface EmployeeDailyActivitiesRepository extends CrudRepository<EmployeeDailyActivities, Long> {

}
