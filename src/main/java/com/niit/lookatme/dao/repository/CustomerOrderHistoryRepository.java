package com.niit.lookatme.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.customer.dao.CustomerOrderHistory;

@Repository("customerOrderHistoryRepository")
public interface CustomerOrderHistoryRepository extends CrudRepository<CustomerOrderHistory, Long> {

	@Query("select c from CustomerOrderHistory c where c.requestId = :requestId")
	CustomerOrderHistory findByRequestId(@Param("requestId") String requestId);
}
