package com.niit.lookatme.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.customer.dao.Customer;
/**
 * 
 * @author Konika
 *
 */

@Repository("customerRepository")
public interface CustomerRepository extends CrudRepository<Customer, Long> {

	@Query("Select c from Customer c where c.dob BETWEEN :first AND :last ORDER BY DOB ASC")
	List<Customer> findAllByDobBetweenOrderByDobAsc(@Param("first") Date first, @Param("last") Date last);
}
