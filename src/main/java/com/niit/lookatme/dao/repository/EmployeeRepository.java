package com.niit.lookatme.dao.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.employee.Employee;
/**
 * 
 * @author Konika
 *
 */

@Repository("employeeRepository")
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
	
	@Query("Select e from Employee e where e.leavingDate >= :today AND e.dob BETWEEN :first AND :last ORDER BY DOB ASC")
	List<Employee> findAllByDobBetweenAndLeavingDateGreaterThanOrEqualToOrderByDobAsc(@Param("first") Date first, @Param("last") Date last, @Param("today") Date today);

	@Query("Select e from Employee e where e.username = :username")
	Optional<Employee> findByUsername(@Param("username") String username);
	
	@Query("select case when count(e)> 0 then true else false end from Employee e where e.username = :username")
	Boolean existsByUsername(@Param("username") String username);
	
	@Query("select e.username from Employee e where lower(e.username) like lower(CONCAT(:username,'%'))")
	List<String> findAllUsernameStartsWith(@Param("username") String username);
}
