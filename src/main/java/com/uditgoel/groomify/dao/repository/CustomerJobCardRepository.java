package com.uditgoel.groomify.dao.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.customer.CustomerJobCard;

@Repository("customerJobCardRepository")
public interface CustomerJobCardRepository extends JpaRepository<CustomerJobCard, Long> {

	@Query("select c from CustomerJobCard c where c.jobId = :jobId and c.jobStatus = com.uditgoel.groomify.dao.JobStatus.ENQUIRY")
	CustomerJobCard findEnquiryJobCardByJobId(@Param("jobId") String jobId);
}
