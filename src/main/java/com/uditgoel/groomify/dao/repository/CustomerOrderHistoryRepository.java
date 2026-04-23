package com.uditgoel.groomify.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.customer.CustomerOrderHistory;

@Repository("customerOrderHistoryRepository")
public interface CustomerOrderHistoryRepository extends JpaRepository<CustomerOrderHistory, Long> {

	@Query("select c from CustomerOrderHistory c where c.requestId = :requestId")
	CustomerOrderHistory findByRequestId(@Param("requestId") String requestId);
}
