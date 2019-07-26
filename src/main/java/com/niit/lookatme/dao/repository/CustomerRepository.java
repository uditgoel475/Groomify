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
@Repository("customerRepository")
public interface CustomerRepository<Customer, ID extends Serializable> extends JpaRepository<Customer, ID> {

	List<Customer> findCustomersByBirthdayCurrentWeek();
	
}
