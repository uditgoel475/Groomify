package com.niit.lookatme.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.customer.dao.CustomerOrderHistory;

@Repository("customerOrderHistoryRepository")
public interface CustomerOrderHistoryRepository extends CrudRepository<CustomerOrderHistory, Long> {

}
