package com.niit.lookatme.facade.helper;

import java.time.DayOfWeek;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dto.AddressInput;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.employee.dao.EmployeeQualification;
import com.niit.lookatme.employee.dao.EmployeeRoster;
import com.niit.lookatme.employee.dto.EmployeeInput;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Component("employeeFacadeHelper")
public class EmployeeFacadeHelper {
	
	@Resource
	private EmployeeRepository employeeRepository;

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

		EmployeeRoster empRoster = new EmployeeRoster();
		empRoster.setInTime(createEmployeeInput.getGenericInTime());
		empRoster.setOutTime(createEmployeeInput.getGenericOutTime());
		empRoster.setWeekStartDay(DayOfWeek.valueOf(createEmployeeInput.getShiftStartDay()));
		empRoster.setWeekEndDay(DayOfWeek.valueOf(createEmployeeInput.getShiftEndDay()));

		employee.setSchedule(empRoster);

		EmployeeQualification empQualif = new EmployeeQualification();
		empQualif.setQualificationType(createEmployeeInput.getQualificationType());
		employee.setQualification(empQualif);

		employee.setPrimaryContact(createEmployeeInput.getPrimaryContact());
		employee.setSecondaryContact(createEmployeeInput.getSecondaryContact());
		employee.setWhatsappContact(createEmployeeInput.getWhatsappContact());
		employee.setEmail(createEmployeeInput.getEmail());

		if (StringUtils.isEmpty(createEmployeeInput.getRegId())) {
			createEmployeeInput.setRegId(CustomerAndEmployeeUtils.createRegId(UserType.EMPLOYEE.toString(),
					createEmployeeInput.getUsername()));
		}
		employee.setRegId(createEmployeeInput.getRegId());
		employee.setGender(Gender.valueOf(createEmployeeInput.getGender()));

		AddressInput addressInput = createEmployeeInput.getCurrentAddress();

		Address currentAddress = CustomerAndEmployeeUtils.populateAddressObject(addressInput);
		employee.setCurrentAddress(currentAddress);
		employee.setOvertimeWorker(createEmployeeInput.isAvailableOvertime());
		employee.setPermanentAddress(createEmployeeInput.isSamePermanent() ? currentAddress
				: CustomerAndEmployeeUtils.populateAddressObject(createEmployeeInput.getPermanentAddress()));

		GovtIdType govtIdType = new GovtIdType();
		govtIdType.setTypeName(createEmployeeInput.getGovtIdType());
		employee.setGovtIdType(govtIdType);
		employee.setGovtId(createEmployeeInput.getGovtId());
		employee.setSalary(createEmployeeInput.getSalary());
		employee.setJoiningDate(createEmployeeInput.getJoiningDate());
		employee.setAdminUser(createEmployeeInput.isAdminUser());
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
