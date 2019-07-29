package com.niit.lookatme.facade.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dto.AddressInput;
import com.niit.lookatme.customer.dto.CreateEmployeeInput;
import com.niit.lookatme.dao.Address;
import com.niit.lookatme.dao.Employee;
import com.niit.lookatme.dao.EmployeeQualification;
import com.niit.lookatme.dao.EmployeeRoster;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;
import com.niit.lookatme.facade.CustomerAndEmployeeFacade;

@Service("customerAndEmployeeFacade")
public class CustomerAndEmployeeFacadeImpl implements CustomerAndEmployeeFacade {

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private EmployeeRepository employeeRepository;

	@Override
	public CustomerAndEmployeeDataDTO fetchAllCustomerAndEmployeeBirthdays() {

		Date startDate = Date.from((LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());

		List<Customer> customersWithBirthdayWeek = customerRepository.findAllByDobBetweenOrderByDobAsc(startDate,
				endDate);

		List<Employee> employeesWithBirthdayWeek = employeeRepository
				.findAllByDobBetweenAndLeavingDateGreaterThanOrEqualToOrderByDobAsc(startDate, endDate,
						Calendar.getInstance().getTime());

		return new CustomerAndEmployeeDataDTO(employeesWithBirthdayWeek, customersWithBirthdayWeek);
	}

	@Override
	public String createNewEmployee(CreateEmployeeInput createEmployeeInput) {

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
			createEmployeeInput.setRegId(createEmployeeRegId(createEmployeeInput.getUsername()));
		}
		employee.setRegId(createEmployeeInput.getRegId());
		employee.setGender(Gender.valueOf(createEmployeeInput.getGender()));

		AddressInput addressInput = createEmployeeInput.getCurrentAddress();

		Address currentAddress = populateAddressObject(addressInput);
		employee.setCurrentAddress(currentAddress);
		employee.setOvertimeWorker(createEmployeeInput.isAvailableOvertime());
		employee.setPermanentAddress(createEmployeeInput.isSamePermanent() ? currentAddress
				: populateAddressObject(createEmployeeInput.getPermanentAddress()));

		GovtIdType govtIdType = new GovtIdType();
		govtIdType.setTypeName(createEmployeeInput.getGovtIdType());
		employee.setGovtIdType(govtIdType);
		employee.setGovtId(createEmployeeInput.getGovtId());
		employee.setSalary(createEmployeeInput.getSalary());
		employee.setJoiningDate(createEmployeeInput.getJoiningDate());
		employee.setAdminUser(createEmployeeInput.isAdminUser());

		Employee empUpdate = employeeRepository.save(employee);
		if (empUpdate.getId() != null) {

			StringBuilder builder = new StringBuilder();
			if (Optional.ofNullable(createEmployeeInput.getPictureFile()).map(MultipartFile::getSize)
					.map(x -> Boolean.valueOf(x > 0)).orElse(false)
					&& uploadPictureImage(createEmployeeInput.getPictureFile(), createEmployeeInput.getUsername(),
							"profile")) {
				String fileExtension = FilenameUtils
						.getExtension(createEmployeeInput.getPictureFile().getOriginalFilename());
				builder.setLength(0);
				builder.append("employees/").append(createEmployeeInput.getUsername())
						.append("/profile/").append(createEmployeeInput.getUsername()).append("_profile.")
						.append(fileExtension);
				empUpdate.setPictureUrl(builder.toString());
			}
			
			if (Optional.ofNullable(createEmployeeInput.getGovtIdPic()).map(MultipartFile::getSize)
					.map(x -> Boolean.valueOf(x > 0)).orElse(false)
					&& uploadPictureImage(createEmployeeInput.getGovtIdPic(), createEmployeeInput.getUsername(),
							"govtId")) {
				String fileExtension = FilenameUtils
						.getExtension(createEmployeeInput.getGovtIdPic().getOriginalFilename());
				builder.setLength(0);
				builder.append("employees/").append(createEmployeeInput.getUsername())
						.append("/govtId/").append(createEmployeeInput.getUsername()).append("_govtId.")
						.append(fileExtension);
				empUpdate.setGovtIdSnapUrl(builder.toString());
			}
			employeeRepository.save(empUpdate);
			return empUpdate.getUsername();
		}
		return "";
	}

	private Address populateAddressObject(AddressInput addressInput) {
		Address currentAddress = new Address();
		currentAddress.setAddress1(addressInput.getAddress1());
		currentAddress.setAddress2(addressInput.getAddress2());
		currentAddress.setAddress3(addressInput.getAddress3());
		currentAddress.setCity(addressInput.getCity());
		currentAddress.setRegion(addressInput.getRegion());
		currentAddress.setState(addressInput.getState());
		currentAddress.setCountry(addressInput.getCountry());
		currentAddress.setPostalCode(addressInput.getPostalCode());
		return currentAddress;
	}

	private String createEmployeeRegId(String username) {
		return DateTimeFormatter.ofPattern("yyyymmddHHmmss").format(LocalDate.now()) + username;
	}

	private String createEmployeeUsername(CreateEmployeeInput employee) {
		long employeecount = employeeRepository.count();
		String initString = employee.getfName().substring(0, 3)
				+ (employee.getmName().isEmpty() ? "0" : employee.getmName().substring(0, 1))
				+ employee.getlName().substring(0, 3);
		initString = StringUtils.rightPad(initString, 13, '0');
		return StringUtils.rightPad(initString, 13, String.valueOf(employeecount));
	}

	private String createCustomerUsername(Customer customer) {
		long employeecount = employeeRepository.count();
		String initString = customer.getFname().substring(0, 3)
				+ (customer.getMname().isEmpty() ? "0" : customer.getMname().substring(0, 1))
				+ customer.getLname().substring(0, 3);
		initString = StringUtils.rightPad(initString, 13, '0');
		return StringUtils.rightPad(initString, 13, String.valueOf(employeecount));
	}

	@Override
	public String createNewCustomer(Customer customer) {
		if (StringUtils.isEmpty(customer.getUsername()))
			customer.setUsername(createCustomerUsername(customer));
		Customer custUpdate = customerRepository.save(customer);
		if (custUpdate.getId() != null) {
			return custUpdate.getUsername();
		}
		return "";
	}

	@Override
	public Boolean uploadPictureImage(MultipartFile file, String empNo, String typeName) {
		try {
			byte[] fileBytes = file.getBytes();
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());

			StringBuilder builder = new StringBuilder("employees/").append(empNo).append("/" + typeName + "/");

			Path directoryPath = Paths.get(builder.toString());
			String fileRelativePathName = builder.append(empNo).append('_').append(typeName).append('.')
					.append(fileExtension).toString();
			if (!directoryPath.toFile().exists()) {
				Files.createDirectories(directoryPath);
				Files.write(Paths.get(fileRelativePathName), fileBytes);
				return true;
			}
			if (Files.deleteIfExists(Paths.get(fileRelativePathName))) {
				Files.write(Paths.get(fileRelativePathName), fileBytes);
				return true;
			}
		} catch (IOException ex) {
			// craete new inputfileempty exception
		}
		return false;
	}

}
