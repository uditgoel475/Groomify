package com.niit.lookatme.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.employee.Employee;
import com.niit.lookatme.dao.services.EmployeeServices;

@Repository("employeeServicesRepository")
public interface EmployeeServicesRepository extends PagingAndSortingRepository<EmployeeServices, Long> {

	@Query("select e from EmployeeServices es INNER JOIN es.service s INNER JOIN es.employee e where "
			+ "e.username in (:employeeUsernames) and s.name = :service order by e.username")
	List<Employee> findAllEmployeesMappedToService(@Param("employeeUsernames") List<String> employeeUsernames, @Param("service") String service);
}
