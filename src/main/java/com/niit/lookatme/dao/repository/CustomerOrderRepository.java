package com.niit.lookatme.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.dao.JobStatus;

@Repository("customerOrderRepository")
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, Long> {

	List<CustomerOrder> findAllByRequestStatusInAndCustomer_Username(List<JobStatus> requestStatus, String username);

	@Query("select c from CustomerOrder c where "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestEndTime >= current_date and "
			+ "c.requestStatus in (com.niit.lookatme.dao.JobStatus.PENDING, com.niit.lookatme.dao.JobStatus.INPROGRESS) "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findAllCalendarMonthOpenAppointment(@Param("date") Date date);

	@Query("select c from CustomerOrder c LEFT JOIN FETCH c.customer t where "
			+ "EXTRACT (day from c.appointmentDate) = EXTRACT(day from :date) and "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestStatus in (com.niit.lookatme.dao.JobStatus.PENDING, com.niit.lookatme.dao.JobStatus.INPROGRESS) and "
			+ "t.username = :username "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findCustomerCalendarOpenAppointmentGivenDate(String username, Date date);

	@Query("select c from CustomerOrder c LEFT JOIN FETCH c.customer t where "
			+ "EXTRACT (day from c.appointmentDate) = EXTRACT(day from :date) and "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestStatus = com.niit.lookatme.dao.JobStatus.ENQUIRY and "
			+ "t.username = :username "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findAllCustomerEnquiryGivenDate(String username, Date date);

	@Query("select c from CustomerOrder c LEFT JOIN FETCH c.customer t where "
			+ "c.requestStatus = com.niit.lookatme.dao.JobStatus.ENQUIRY and "
			+ "t.username = :username "
			+ "order by c.appointmentDate")
	List<CustomerOrder> fetchAllCustomerEnquiries(String custNo);

	@Query("select c from CustomerOrder c where "
			+ "EXTRACT (day from c.appointmentDate) = EXTRACT(day from :date) and "
			+ "EXTRACT (month from c.appointmentDate) = EXTRACT(month from :date) and "
			+ "EXTRACT (year from c.appointmentDate) = EXTRACT(year from :date) and "
			+ "c.requestStatus = com.niit.lookatme.dao.JobStatus.ENQUIRY "
			+ "order by c.appointmentDate")
	List<CustomerOrder> fetchAllEnquiriesGivenDate(Date date);
	
	CustomerOrder findByRequestId(String requestId);

	@Query("select c from CustomerOrder c where "
			+ "c.appointmentDate >= :startDate and "
			+ "c.appointmentDate <= :endDate and "
			+ "c.requestStatus = com.niit.lookatme.dao.JobStatus.ENQUIRY and "
			+ "order by c.appointmentDate")
	List<CustomerOrder> findAllCustomerEnquiriesDateRange(Date startDate, Date endDate);
}
