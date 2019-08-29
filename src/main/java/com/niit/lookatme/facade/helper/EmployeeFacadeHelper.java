package com.niit.lookatme.facade.helper;

import java.time.LocalDate;
import java.util.Collections;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.Password;
import com.niit.lookatme.dao.employee.Employee;
import com.niit.lookatme.dao.employee.EmployeeQualification;
import com.niit.lookatme.dao.employee.EmployeeRoster;
import com.niit.lookatme.dao.repository.EmployeeQualificationRepository;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dao.repository.EmployeeRosterRepository;
import com.niit.lookatme.dao.repository.GovtIdTypeRepository;
import com.niit.lookatme.dao.repository.RoleRepository;
import com.niit.lookatme.dao.role.Role;
import com.niit.lookatme.dto.AddressInput;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.dto.employee.EmployeeDTO;
import com.niit.lookatme.dto.employee.EmployeeInput;
import com.niit.lookatme.dto.employee.Roster;
import com.niit.lookatme.exception.ResourceNotFoundException;
import com.niit.lookatme.utils.Converter;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Component("employeeFacadeHelper")
public class EmployeeFacadeHelper {

	@Resource
	private EmployeeRepository employeeRepository;

	@Resource
	private EmployeeRosterRepository employeeRosterRepository;

	@Resource
	private EmployeeQualificationRepository employeeQualificationRepository;

	@Resource
	private GovtIdTypeRepository govtIdTypeRepository;

	@Resource
	private RoleRepository roleRepository;

	public EmployeeDTO createEmployeeDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO(employee.getName(), employee.getUsername(), employee.getDob(),
				employee.getPrimaryContact(), employee.getGender(),
				CustomerAndEmployeeUtils.populateAddressOut(employee.getCurrentAddress()),
				CustomerAndEmployeeUtils.populateAddressOut(employee.getPermanentAddress()));
		employeeDTO.setRegId(employee.getRegId());
		employeeDTO.setEmail(employee.getEmail());
		return employeeDTO;
	}

	public Employee createEmployeeJPAFromEmployeeInput(EmployeeInput createEmployeeInput) {
		Employee employee = new Employee();
		employee.setFname(createEmployeeInput.getfName());
		employee.setMname(createEmployeeInput.getmName());
		employee.setLname(createEmployeeInput.getlName());
		employee.setDob(createEmployeeInput.getDob());
		if (StringUtils.isEmpty(createEmployeeInput.getUsername())) {
			createEmployeeInput.setUsername(createEmployeeUsername(createEmployeeInput));
		}
		employee.setUsername(createEmployeeInput.getUsername());

		if (null != createEmployeeInput.getRoster()) {
			Roster roster = createEmployeeInput.getRoster();

			StringBuilder rosterStr = new StringBuilder();
			rosterStr.append(roster.getShiftStartDay()).append(" - ").append(roster.getShiftEndDay()).append(" ")
					.append(roster.getGenericInTime()).append(" - ").append(roster.getGenericOutTime());
			EmployeeRoster empRoster = employeeRosterRepository
					.findActiveRosterByGivenInput(roster.getGenericInTime(), roster.getGenericOutTime(),
							roster.getShiftStartDay(), roster.getShiftEndDay())
					.orElseThrow(
							() -> new ResourceNotFoundException("Employee Roster", "Schedule", rosterStr.toString()));

			employee.setSchedule(empRoster);

		}

		if (null != createEmployeeInput.getQualificationType()) {
			EmployeeQualification employeeQualification = employeeQualificationRepository
					.findByQualificationType(createEmployeeInput.getQualificationType())
					.orElseThrow(() -> new ResourceNotFoundException("Employee Qualification Type", "Value",
							createEmployeeInput.getQualificationType().toString()));
			employee.setQualification(employeeQualification);
		}

		employee.setPrimaryContact(createEmployeeInput.getPrimaryContact());
		employee.setSecondaryContact(createEmployeeInput.getSecondaryContact());
		employee.setWhatsappContact(createEmployeeInput.getWhatsappContact());
		employee.setEmail(createEmployeeInput.getEmail());

		if (StringUtils.isEmpty(createEmployeeInput.getRegId())) {
			createEmployeeInput.setRegId(CustomerAndEmployeeUtils.createRegId(UserType.EMPLOYEE.toString(),
					createEmployeeInput.getUsername()));
		}
		employee.setRegId(createEmployeeInput.getRegId());
		employee.setGender(createEmployeeInput.getGender());

		AddressInput addressInput = createEmployeeInput.getCurrentAddress();
		if (null != addressInput) {
			Address currentAddress = CustomerAndEmployeeUtils.populateAddressObject(addressInput);
			employee.setCurrentAddress(currentAddress);
			employee.setOvertimeWorker(createEmployeeInput.isAvailableOvertime());
			employee.setPermanentAddress(createEmployeeInput.isSamePermanent() ? currentAddress
					: CustomerAndEmployeeUtils.populateAddressObject(createEmployeeInput.getPermanentAddress()));
		}

		if (!StringUtils.isEmpty(createEmployeeInput.getGovtIdType())
				&& !StringUtils.isEmpty(createEmployeeInput.getGovtId())) {
			GovtIdType govtIdType = govtIdTypeRepository.findByTypeName(createEmployeeInput.getGovtIdType())
					.orElseThrow(() -> new ResourceNotFoundException("Government ID", "Type",
							createEmployeeInput.getGovtIdType()));
			if (!StringUtils.isEmpty(govtIdType.getRegex())
					&& !createEmployeeInput.getGovtId().matches(govtIdType.getRegex())) {
				throw new IllegalArgumentException(String.format("Government ID '%s' has an invalid value : '%s' ",
						createEmployeeInput.getGovtIdType(), createEmployeeInput.getGovtId()));
			}
			employee.setGovtIdType(govtIdType);
			employee.setGovtId(createEmployeeInput.getGovtId());
		}

		employee.setSalary(createEmployeeInput.getSalary());

		if (null == createEmployeeInput.getJoiningDate()) {
			createEmployeeInput.setJoiningDate(Converter.convertLocalDateToDate(LocalDate.now()));
		}
		employee.setJoiningDate(createEmployeeInput.getJoiningDate());

		Role role = roleRepository.findByName(createEmployeeInput.getRoleName())
				.orElseThrow(() -> new IllegalArgumentException(
						String.format("Role Name '%s' not present", createEmployeeInput.getRoleName())));

		employee.setEmployeeRoles(Collections.singleton(role));
		if (!StringUtils.isEmpty(createEmployeeInput.getPassword())) {
			Password password = new Password();
			password.setPassword(createEmployeeInput.getPassword(), UserType.CUSTOMER);
			
			employee.setPassword(password);
		}

		return employee;
	}

	private String createEmployeeUsername(EmployeeInput employee) {
		long employeecount = employeeRepository.count();
		String initString = employee.getfName().substring(0, 3)
				+ (employee.getmName().isEmpty() ? "0" : employee.getmName().substring(0, 1))
				+ employee.getlName().substring(0, 3);
		initString = StringUtils.rightPad(initString, 13, '0');
		return StringUtils.rightPad(initString, 13, String.valueOf(employeecount));
	}
}
