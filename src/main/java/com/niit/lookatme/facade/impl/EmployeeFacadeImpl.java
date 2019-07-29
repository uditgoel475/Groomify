package com.niit.lookatme.facade.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dao.EmployeeQualification;
import com.niit.lookatme.dao.EmployeeRoster;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dto.AddressInput;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dto.EmployeeInput;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Service("employeeFacade")
public class EmployeeFacadeImpl implements EmployeeFacade {

	@Resource
	private EmployeeRepository employeeRepository;

	@Override
	public List<Employee> fetchAllExistingEmployeeCurrentWeekBirthdays() {

		Date startDate = Date.from((LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
		return employeeRepository.findAllByDobBetweenAndLeavingDateGreaterThanOrEqualToOrderByDobAsc(startDate, endDate,
				Calendar.getInstance().getTime());
	}

	@Override
	public String createNewEmployee(EmployeeInput createEmployeeInput) {

		Employee employee = employeeRepository.save(createEmployeeJPAFromEmployeeInput(createEmployeeInput));
		if (employee.getId() != null) {

			employee.setPictureUrl(setImageUrl(UserImageInputType.PROFILE, createEmployeeInput));

			employee.setGovtIdSnapUrl(setImageUrl(UserImageInputType.GOVTID, createEmployeeInput));

			if (!StringUtils.isEmpty(employee.getPictureUrl()) || !StringUtils.isEmpty(employee.getGovtIdSnapUrl()))
				employeeRepository.save(employee);
			return employee.getUsername();
		}
		return StringUtils.EMPTY;
	}

	private Employee createEmployeeJPAFromEmployeeInput(EmployeeInput createEmployeeInput) {
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
			createEmployeeInput.setRegId(
					CustomerAndEmployeeUtils.createRegId(UserType.EMPLOYEE, createEmployeeInput.getUsername()));
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

	private String setImageUrl(UserImageInputType userImageInputType, EmployeeInput createEmployeeInput) {
		if (Optional.ofNullable(createEmployeeInput.getPictureFile()).map(MultipartFile::getSize)
				.map(x -> Boolean.valueOf(x > 0)).orElse(false)) {
			String filePathName = CustomerAndEmployeeUtils.uploadPictureImage(UserType.EMPLOYEE,
					createEmployeeInput.getPictureFile(), createEmployeeInput.getUsername(),
					userImageInputType);
			if (!StringUtils.isEmpty(filePathName)) {
				return filePathName;
			}
		}
		return null;
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
