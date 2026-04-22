package com.uditgoel.groomify.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.employee.EmployeeQualification;
import com.uditgoel.groomify.dao.employee.Qualifications;

@Repository("employeeQualificationRepository")
public interface EmployeeQualificationRepository extends PagingAndSortingRepository<EmployeeQualification, Long> {

	Optional<EmployeeQualification> findByQualificationType(Qualifications qualificationType);
}
