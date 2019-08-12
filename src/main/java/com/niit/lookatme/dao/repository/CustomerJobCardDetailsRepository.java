package com.niit.lookatme.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.customer.CustomerJobCardDetails;

@Repository("customerJobCardDetailsRepository")
public interface CustomerJobCardDetailsRepository extends CrudRepository<CustomerJobCardDetails, Long> {

	@Query("Select c from CustomerJobCardDetails c LEFT JOIN FETCH c.jobId t where t.jobId = :jobId and c.subJobId in (:subJobIdList) order by c.jobStartTime")
	List<CustomerJobCardDetails> findBySubJobIdAndJobId(@Param("subJobIdList") List<String> subJobIdList, @Param("jobId") String jobId);
}
