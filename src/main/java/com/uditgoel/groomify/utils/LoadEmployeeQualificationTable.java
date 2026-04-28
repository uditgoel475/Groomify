package com.uditgoel.groomify.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.uditgoel.groomify.dao.employee.EmployeeQualification;
import com.uditgoel.groomify.dao.employee.Qualifications;
import com.uditgoel.groomify.dao.repository.EmployeeQualificationRepository;

@Component
public class LoadEmployeeQualificationTable {

	private final EmployeeQualificationRepository employeeQualificationRepository;

	public LoadEmployeeQualificationTable(EmployeeQualificationRepository employeeQualificationRepository) {
		this.employeeQualificationRepository = employeeQualificationRepository;
	}

	@PostConstruct
	public void init() {

		if (employeeQualificationRepository.count() == 0) {
			List<Qualifications> qualificationsList = new ArrayList<>(EnumSet.allOf(Qualifications.class));

			employeeQualificationRepository
					.saveAll(qualificationsList.stream().map(EmployeeQualification::new).collect(Collectors.toList()));
		}
	}
}
