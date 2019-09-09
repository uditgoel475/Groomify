package com.niit.lookatme.facade.helper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${employee.fname.default}")
	private String defaultEmpName;

	public EmployeeDTO createEmployeeDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO(employee.getName(), employee.getUsername(), employee.getDob(),
				employee.getPrimaryContact(), employee.getGender(),
				CustomerAndEmployeeUtils.populateAddressOut(employee.getCurrentAddress()),
				CustomerAndEmployeeUtils.populateAddressOut(employee.getPermanentAddress()));
		employeeDTO.setRegId(employee.getRegId());
		employeeDTO.setEmail(employee.getEmail());
		return employeeDTO;
	}

	public List<EmployeeDTO> createEmployeeDTOList(List<Employee> employeeList) {
		return employeeList.stream().map(this::createEmployeeDTO).collect(Collectors.toList());
	}

	public Employee createEmployeeJPAFromEmployeeInput(EmployeeInput createEmployeeInput) {
		Employee employee = new Employee();

		if (!StringUtils.isEmpty(createEmployeeInput.getName())) {
			List<String> splitName = CustomerAndEmployeeUtils.getSplittedNameArr(createEmployeeInput.getName());
			employee.setFname(splitName.get(0));
			if (StringUtils.isEmpty(splitName.get(1))) {
				employee.setMname(splitName.get(1));
			}
			if (StringUtils.isEmpty(splitName.get(2))) {
				employee.setLname(splitName.get(2));
			}
		}

		employee.setDob(createEmployeeInput.getDob());
		if (StringUtils.isEmpty(createEmployeeInput.getUsername())) {
			createEmployeeInput.setUsername(createEmployeeUsername(createEmployeeInput));
		}
		employee.setUsername(createEmployeeInput.getUsername());

		if (null != createEmployeeInput.getRoster()) {
			employee.setSchedule(findEmployeeRoster(createEmployeeInput.getRoster()));
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
			employee.setGovtIdType(validateAndGetGovtIdType(createEmployeeInput));
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
		Password password = new Password();
		password.setPassword(createEmployeeInput.getPassword(), UserType.CUSTOMER);

		employee.setPassword(password);

		return employee;
	}

	private GovtIdType validateAndGetGovtIdType(EmployeeInput createEmployeeInput) {
		GovtIdType govtIdType = govtIdTypeRepository
				.findByTypeNameOrderByTypeNameAsc(createEmployeeInput.getGovtIdType())
				.orElseThrow(() -> new ResourceNotFoundException("Government ID", "Type",
						createEmployeeInput.getGovtIdType()));
		if (!StringUtils.isEmpty(govtIdType.getRegex())
				&& !createEmployeeInput.getGovtId().matches(govtIdType.getRegex())) {
			throw new IllegalArgumentException(String.format("Government ID '%s' has an invalid value : '%s' ",
					createEmployeeInput.getGovtIdType(), createEmployeeInput.getGovtId()));
		}
		return govtIdType;
	}

	private EmployeeRoster findEmployeeRoster(Roster roster) {
		StringBuilder rosterStr = new StringBuilder();
		rosterStr.append(roster.getShiftStartDay()).append(" - ").append(roster.getShiftEndDay()).append(" ")
				.append(roster.getGenericInTime()).append(" - ").append(roster.getGenericOutTime());
		return employeeRosterRepository
				.findActiveRosterByGivenInput(roster.getGenericInTime(), roster.getGenericOutTime(),
						roster.getShiftStartDay(), roster.getShiftEndDay())
				.orElseThrow(() -> new ResourceNotFoundException("Employee Roster", "Schedule", rosterStr.toString()));
	}

	private String createEmployeeUsername(EmployeeInput employee) {
		long employeecount = employeeRepository.count();

		StringBuilder userNameBuilder = new StringBuilder();
		if (StringUtils.isEmpty(employee.getName())) {
			userNameBuilder.append(defaultEmpName).append('.').append(employeecount).toString();
		} else {
			List<String> splitName = CustomerAndEmployeeUtils.getSplittedNameArr(employee.getName());
			userNameBuilder.append(splitName.get(0));
			if (!StringUtils.isEmpty(splitName.get(1)))
				userNameBuilder.append('.').append(splitName.get(1));
			if (!StringUtils.isEmpty(splitName.get(2)))
				userNameBuilder.append('.').append(splitName.get(2));
			userNameBuilder.append('.');
		}

		List<String> matchingUsername = employeeRepository.findAllUsernameStartsWith(userNameBuilder.toString());
		while (matchingUsername.contains(userNameBuilder.toString().concat(String.valueOf(employeecount)))) {
			userNameBuilder.append(0);
		}

		return userNameBuilder.append(employeecount).toString();

	}
}
