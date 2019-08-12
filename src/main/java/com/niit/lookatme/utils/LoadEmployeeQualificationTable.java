package com.niit.lookatme.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.niit.lookatme.dao.employee.EmployeeQualification;
import com.niit.lookatme.dao.employee.Qualifications;
import com.niit.lookatme.dao.repository.EmployeeQualificationRepository;

@Component
public class LoadEmployeeQualificationTable {

	@Resource
	private EmployeeQualificationRepository employeeQualificationRepository;

	@PostConstruct
	public void init() {

		if (employeeQualificationRepository.count() == 0) {
			List<Qualifications> qualificationsList = new ArrayList<>(EnumSet.allOf(Qualifications.class));

			employeeQualificationRepository
					.saveAll(qualificationsList.stream().map(EmployeeQualification::new).collect(Collectors.toList()));
		}
	}
}
