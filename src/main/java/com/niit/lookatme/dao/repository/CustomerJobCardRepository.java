package com.niit.lookatme.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.customer.CustomerJobCard;

@Repository("customerJobCardRepository")
public interface CustomerJobCardRepository extends PagingAndSortingRepository<CustomerJobCard, Long> {

	@Query("select c from CustomerJobCard c where c.jobId = :jobId and c.jobStatus = com.niit.lookatme.dao.JobStatus.ENQUIRY")
	CustomerJobCard findEnquiryJobCardByJobId(@Param("jobId") String jobId);
}
