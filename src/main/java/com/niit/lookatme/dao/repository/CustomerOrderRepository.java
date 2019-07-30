package com.niit.lookatme.dao.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.dao.JobStatus;

@Repository("customerOrderRepository")
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {

	List<CustomerOrder> findAllByRequestStatusInAndCustomer_Username(List<JobStatus> requestStatus, String username);
}
