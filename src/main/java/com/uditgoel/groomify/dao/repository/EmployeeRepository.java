package com.uditgoel.groomify.dao.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.employee.Employee;

/**
 * 
 * @author Konika
 *
 */

@Repository("employeeRepository")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("Select e from Employee e where e.leavingDate >= :today AND e.dob BETWEEN :first AND :last ORDER BY e.dob ASC")
	List<Employee> findAllByDobBetweenAndLeavingDateGreaterThanOrEqualToOrderByDobAsc(@Param("first") Date first,
			@Param("last") Date last, @Param("today") Date today);

	@Query("Select e from Employee e where e.username = :username")
	Optional<Employee> findByUsername(@Param("username") String username);

	@Query("select case when count(e)> 0 then true else false end from Employee e where e.username = :username")
	Boolean existsByUsername(@Param("username") String username);

	@Query("select e.username from Employee e where lower(e.username) like lower(CONCAT(:username,'%')) Order by e.username")
	List<String> findAllUsernameStartsWith(@Param("username") String username);

	@Query("select e from Employee e where lower(e.fname) like lower(CONCAT(:first,'%')) order by e.fname asc")
	List<Employee> findAllMatchingFirstName(@Param("first") String first);

	@Query("select e from Employee e where lower(e.fname) like lower(CONCAT(:first,'%')) and lower(e.lname) like lower(CONCAT(:last,'%'))")
	List<Employee> findAllMatchingFNameLName(@Param("first") String first, @Param("last") String last, Sort sort);

	@Query("select e from Employee e where lower(e.fname) like lower(CONCAT(:first,'%')) and lower(e.mname) like lower(CONCAT(:middle,'%')) and lower(e.lname) like lower(CONCAT(:last,'%'))")
	List<Employee> findAllMatchingName(@Param("first") String first, @Param("middle") String middle,
			@Param("last") String last, Sort sort);
}
