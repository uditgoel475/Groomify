package com.niit.lookatme.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.employee.EmployeeQualification;
import com.niit.lookatme.dao.employee.Qualifications;

@Repository("employeeQualificationRepository")
public interface EmployeeQualificationRepository extends PagingAndSortingRepository<EmployeeQualification, Long> {

	Optional<EmployeeQualification> findByQualificationType(Qualifications qualificationType);
}
