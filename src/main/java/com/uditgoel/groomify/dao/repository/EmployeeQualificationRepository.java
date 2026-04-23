package com.uditgoel.groomify.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.employee.EmployeeQualification;
import com.uditgoel.groomify.dao.employee.Qualifications;

@Repository("employeeQualificationRepository")
public interface EmployeeQualificationRepository extends JpaRepository<EmployeeQualification, Long> {

	Optional<EmployeeQualification> findByQualificationType(Qualifications qualificationType);
}
