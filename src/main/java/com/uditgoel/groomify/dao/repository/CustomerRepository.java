package com.uditgoel.groomify.dao.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.customer.Customer;

/**
 * 
 * @author Konika
 *
 */

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query("Select c from Customer c where c.dob BETWEEN :first AND :last ORDER BY DOB ASC")
	List<Customer> findAllByDobBetweenOrderByDobAsc(@Param("first") Date first, @Param("last") Date last);

	@Query("Select c from Customer c where c.username = :username")
	Optional<Customer> findByUsername(@Param("username") String username);

	@Query("select c.username from Customer c where lower(c.username) like lower(CONCAT(:username,'%')) order by c.username")
	List<String> findAllUsernameStartsWith(@Param("username") String username);

	@Query("select case when count(c)> 0 then true else false end from Customer c where c.username = :username")
	Boolean existsByUsername(@Param("username") String username);

	@Query("select case when count(e)> 0 then true else false end from Employee e where e.email = :email")
	Boolean existsByEmail(@Param("email") String email);

	@Query("select c from Customer c where lower(c.fname) like lower(CONCAT(:first,'%')) order by c.fname asc")
	List<Customer> findAllMatchingFirstName(@Param("first") String first);

	@Query("select c from Customer c where lower(c.fname) = lower(CONCAT(:first,'%')) and lower(c.lname) = lower(CONCAT(:last,'%'))")
	List<Customer> findAllMatchingFNameLName(@Param("first") String first, @Param("last") String last, Sort sort);

	@Query("select c from Customer c where lower(c.fname) = lower(CONCAT(:first,'%')) and lower(c.mname) = lower(CONCAT(:middle,'%')) and lower(c.lname) = lower(CONCAT(:last,'%'))")
	List<Customer> findAllMatchingName(@Param("first") String first, @Param("middle") String middle,
			@Param("last") String last, Sort sort);
}
