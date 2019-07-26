package com.niit.lookatme.dao.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * 
 * @author Konika
 *
 */
@Repository("employeeRepository")
public interface EmployeeRepository<Employee, ID extends Serializable> extends JpaRepository<Employee, ID> {

	List<Employee> findEmployeesByBirthdayCurrentWeek();
	
}
