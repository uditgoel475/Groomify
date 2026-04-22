package com.uditgoel.groomify.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.JobStatus;
import com.uditgoel.groomify.dao.customer.CustomerOrder;

@Repository("customerOrderRepository")
public interface CustomerOrderRepository extends PagingAndSortingRepository<CustomerOrder, Long> {

	List<CustomerOrder> findAllByRequestStatusInAndCustomer_Username(List<JobStatus> requestStatus, String username);

	@Query("select c from CustomerOrder c where "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.appointmentDate >= current_date and "
			+ "c.requestStatus in (com.uditgoel.groomify.dao.JobStatus.PENDING, com.uditgoel.groomify.dao.JobStatus.INPROGRESS) "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findAllCalendarMonthOpenAppointment(@Param("date") Date date);

	@Query("select c from CustomerOrder c LEFT JOIN FETCH c.customer t where "
			+ "EXTRACT (day from c.appointmentDate) = EXTRACT(day from :date) and "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestStatus in (com.uditgoel.groomify.dao.JobStatus.PENDING, com.uditgoel.groomify.dao.JobStatus.INPROGRESS) and "
			+ "t.username = :username order by c.appointmentDate")
	List<CustomerOrder> findCustomerCalendarOpenAppointmentGivenDate(@Param("username") String username,
			@Param("date") Date date);

	@Query("select c from CustomerOrder c LEFT JOIN FETCH c.customer t where "
			+ "EXTRACT (day from c.appointmentDate) = EXTRACT(day from :date) and "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestStatus = com.uditgoel.groomify.dao.JobStatus.ENQUIRY and t.username = :username "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findAllCustomerEnquiryGivenDate(@Param("username") String username, @Param("date") Date date);

	@Query("select c from CustomerOrder c LEFT JOIN FETCH c.customer t where "
			+ "c.requestStatus = com.uditgoel.groomify.dao.JobStatus.ENQUIRY and t.username = :username "
			+ "order by c.appointmentDate")
	List<CustomerOrder> fetchAllCustomerEnquiries(@Param("username") String username);

	@Query("select c from CustomerOrder c where "
			+ "EXTRACT (day from c.appointmentDate) = EXTRACT(day from :date) and "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestStatus = com.uditgoel.groomify.dao.JobStatus.ENQUIRY order by c.appointmentDate")
	List<CustomerOrder> fetchAllEnquiriesGivenDate(@Param("date") Date date);

	@Query("select c from CustomerOrder c where c.requestId = :requestId and c.requestStatus = com.uditgoel.groomify.dao.JobStatus.ENQUIRY")
	CustomerOrder findEnquiryByRequestId(@Param("requestId") String requestId);

	@Query("select c from CustomerOrder c where c.appointmentDate >= :startDate and "
			+ "c.appointmentDate <= :endDate and c.requestStatus = com.uditgoel.groomify.dao.JobStatus.ENQUIRY "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findAllCustomerEnquiriesDateRange(@Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	@Query("select c from CustomerOrder c where c.requestId = :requestId and "
			+ "c.requestStatus not in (com.uditgoel.groomify.dao.JobStatus.COMPLETED, "
			+ "com.uditgoel.groomify.dao.JobStatus.CANCELLED)")
	CustomerOrder findCancellableOrderById(@Param("requestId") String requestId);
}
