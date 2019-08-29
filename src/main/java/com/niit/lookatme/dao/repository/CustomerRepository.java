package com.niit.lookatme.dao.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.customer.Customer;
/**
 * 
 * @author Konika
 *
 */

@Repository("customerRepository")
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("Select c from Customer c where c.dob BETWEEN :first AND :last ORDER BY DOB ASC")
	List<Customer> findAllByDobBetweenOrderByDobAsc(@Param("first") Date first, @Param("last") Date last);
	
	@Query("Select c from Customer c where c.username = :username")
	Optional<Customer> findByUsername(@Param("username") String username);
	
	@Query("select case when count(c)> 0 then true else false end from Customer c where c.username = :username")
	Boolean existsByUsername(@Param("username") String username);
	
	@Query("select case when count(e)> 0 then true else false end from Employee e where e.email = :email")
	Boolean existsByEmail(@Param("email") String email);
}
