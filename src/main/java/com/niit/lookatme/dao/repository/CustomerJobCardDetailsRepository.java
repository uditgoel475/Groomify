package com.niit.lookatme.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.customer.dao.CustomerJobCardDetails;

@Repository("customerJobCardDetailsRepository")
public interface CustomerJobCardDetailsRepository extends CrudRepository<CustomerJobCardDetails, Long> {

	@Query("Select c from CustomerJobCardDetails c FETCH JOIN c.jobId j where t.jobId = :jobId and c.subJobId in (:subJobIdList) order by c.jobStartTime")
	List<CustomerJobCardDetails> findBySubJobIdAndJobId(List<String> subJobIdList, String jobId);
}
