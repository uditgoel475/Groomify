package com.uditgoel.groomify.facade.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uditgoel.groomify.dao.Activity;
import com.uditgoel.groomify.dao.Password;
import com.uditgoel.groomify.dao.customer.Customer;
import com.uditgoel.groomify.dao.employee.Employee;
import com.uditgoel.groomify.dao.employee.EmployeeDailyActivities;
import com.uditgoel.groomify.dao.employee.EmployeeRoster;
import com.uditgoel.groomify.dao.repository.CustomerRepository;
import com.uditgoel.groomify.dao.repository.EmployeeDailyActivitiesRepository;
import com.uditgoel.groomify.dao.repository.EmployeeRepository;
import com.uditgoel.groomify.dao.repository.EmployeeServicesRepository;
import com.uditgoel.groomify.dto.UserImageInputType;
import com.uditgoel.groomify.dto.UserType;
import com.uditgoel.groomify.dto.employee.EmployeeActivityOut;
import com.uditgoel.groomify.dto.employee.EmployeeDTO;
import com.uditgoel.groomify.dto.employee.EmployeeInput;
import com.uditgoel.groomify.dto.employee.Roster;
import com.uditgoel.groomify.exception.RequiredLengthException;
import com.uditgoel.groomify.exception.ResourceNotFoundException;
import com.uditgoel.groomify.facade.EmployeeFacade;
import com.uditgoel.groomify.facade.helper.EmployeeFacadeHelper;
import com.uditgoel.groomify.utils.Converter;
import com.uditgoel.groomify.utils.CustomerAndEmployeeUtils;

@Service("employeeFacade")
public class EmployeeFacadeImpl implements EmployeeFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeFacadeImpl.class);

	@Resource
	private EmployeeRepository employeeRepository;

	@Resource
	private EmployeeDailyActivitiesRepository employeeDailyActivitiesRepository;

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private EmployeeFacadeHelper employeeFacadeHelper;

	@Resource
	private EmployeeServicesRepository employeeServicesRepository;

	@Resource
	private PasswordEncoder passwordEncoder;

	@Value("${employee.password.regex}")
	private String passwrdRegex;

	@Value("${employee.password.notes}")
	private String passwrdExceptionMsg;

	@Value("${employee.pass.default}")
	private String passwrdDefault;

	@Value("${employee.name.search.min.length}")
	private int minFNameLength;

	@Override
	public List<EmployeeDTO> fetchAllExistingEmployeeCurrentWeekBirthdays() {

		Date startDate = Date.from((LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
		List<Employee> employeeList = employeeRepository
				.findAllByDobBetweenAndLeavingDateGreaterThanOrEqualToOrderByDobAsc(startDate, endDate,
						Calendar.getInstance().getTime());
		return employeeList.stream().map(x -> employeeFacadeHelper.createEmployeeDTO(x)).collect(Collectors.toList());
	}

	@Override
	public EmployeeDTO fetchEmployeeDTO(String username) {
		Employee employee = findByUsername(username);
		return employeeFacadeHelper.createEmployeeDTO(employee);
	}

	private Employee findByUsername(String username) {
		return employeeRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "username", username));
	}

	@Override
	public String createNewEmployee(EmployeeInput createEmployeeInput) {

		if (!StringUtils.isEmpty(createEmployeeInput.getPassword())) {
			validatePassword(createEmployeeInput.getPassword());
			createEmployeeInput.setPassword(passwordEncoder.encode(createEmployeeInput.getPassword()));
		} else {
			createEmployeeInput.setPassword(passwordEncoder.encode(passwrdDefault));
		}

		Employee employee = employeeRepository
				.save(employeeFacadeHelper.createEmployeeJPAFromEmployeeInput(createEmployeeInput));

		if (employee.getId() != null) {
			return employeeUserNameFromCreatedEmployee(createEmployeeInput, employee);
		}
		return StringUtils.EMPTY;
	}

	private String employeeUserNameFromCreatedEmployee(EmployeeInput createEmployeeInput, Employee employee) {
		if (null != createEmployeeInput.getPictureFile() && createEmployeeInput.getPictureFile().getSize() > 0)
			employee.setPictureUrl(
					CustomerAndEmployeeUtils.createImageAndFetchUrl(UserType.EMPLOYEE, UserImageInputType.PROFILE,
							createEmployeeInput.getPictureFile(), createEmployeeInput.getUsername()));

		if (null != createEmployeeInput.getGovtIdPic() && createEmployeeInput.getGovtIdPic().getSize() > 0)
			employee.setGovtIdSnapUrl(CustomerAndEmployeeUtils.createImageAndFetchUrl(UserType.EMPLOYEE,
					UserImageInputType.GOVTID, createEmployeeInput.getGovtIdPic(), createEmployeeInput.getUsername()));

		if (!StringUtils.isEmpty(employee.getPictureUrl()) || !StringUtils.isEmpty(employee.getGovtIdSnapUrl()))
			employeeRepository.save(employee);
		return employee.getUsername();
	}

	private void validatePassword(String password) {
		if (!password.matches(passwrdRegex)) {
			throw new IllegalArgumentException(String.format("Input password '%s' doesn't pass the strength test. %s",
					password, passwrdExceptionMsg));
		}
	}

	@Override
	public Boolean changeEmployeePassword(String empNo, String currentPass, String newPass) {
		validatePassword(newPass);
		String encryptNew = passwordEncoder.encode(newPass);

		Employee employee = findByUsername(empNo);
		Password passwords = employee.getPassword();

		if (!passwordEncoder.matches(currentPass, passwords.getCurrentPassword())) {
			throw new IllegalArgumentException("Current Password is incorrect.");
		}

		if (passwords.getAllPasswordList().stream().anyMatch(pwd -> passwordEncoder.matches(newPass, pwd))) {
			throw new IllegalArgumentException(
					"Password must not match the last 5 passwords. Please provide a different input");
		}
		passwords.setPassword(encryptNew, UserType.CUSTOMER);
		employee.setPassword(passwords);
		try {
			employeeRepository.save(employee);
			return true;
		} catch (DataAccessException | HibernateException ex) {
			LOGGER.error(ex.getMessage());
			return false;
		}
	}

	private EmployeeDailyActivities createEmployeeActivity(String empNo, Activity activity, String custUsername,
			Date inputTime) {
		EmployeeDailyActivities employeeDailyActivities = new EmployeeDailyActivities();
		employeeDailyActivities.setActivity(activity);
		Employee employee = findByUsername(empNo);
		employeeDailyActivities.setEmployee(employee);
		if (!StringUtils.isEmpty(custUsername)) {
			Customer customer = customerRepository.findByUsername(custUsername)
					.orElseThrow(() -> new ResourceNotFoundException("Customer", "username", empNo));
			employeeDailyActivities.setCustomer(customer);
		}
		employeeDailyActivities.setCreatedBy("anonymous");
		employeeDailyActivities.setLastModifiedBy("anonymous");
		Calendar cal = Calendar.getInstance();
		employeeDailyActivities.setTime(inputTime == null ? cal.getTime() : inputTime);
		employeeDailyActivities.setCreationDate(cal.getTime());
		employeeDailyActivities.setLastModifiedDate(cal.getTime());
		return employeeDailyActivities;
	}

	@Override
	public Boolean employeeFutureActivity(Map<Date, List<String>> employeeActivityMap, Activity activity) {
		List<EmployeeDailyActivities> employeeDailyActivityList = new ArrayList<>();
		employeeActivityMap.entrySet().forEach(
				employeeActivityEntry -> employeeActivityEntry.getValue().forEach(employee -> employeeDailyActivityList
						.add(createEmployeeActivity(employee, activity, null, employeeActivityEntry.getKey()))));
		employeeDailyActivitiesRepository.saveAll(employeeDailyActivityList);
		return true;
	}

	@Override
	public Boolean attendCustomer(String empNo, Activity activity, String customer) {
		EmployeeDailyActivities employeeDailyActivities = createEmployeeActivity(empNo, activity, customer, null);
		employeeDailyActivities = employeeDailyActivitiesRepository.save(employeeDailyActivities);
		return employeeDailyActivities.getId() != null;
	}

	@Override
	public Boolean markActivity(String empNo, Activity activity) {
		EmployeeDailyActivities employeeDailyActivities = createEmployeeActivity(empNo, activity, null, null);
		employeeDailyActivities = employeeDailyActivitiesRepository.save(employeeDailyActivities);
		return employeeDailyActivities.getId() != null;
	}

	@Override
	public Map<Date, List<EmployeeActivityOut>> fetchEmployeeMonthlyAttendance(String empNo, Month month, int year) {

		YearMonth yearMonth = YearMonth.of(year, month);

		List<Activity> activityList = new ArrayList<>();
		activityList.add(Activity.SALON_IN);
		activityList.add(Activity.SALON_OUT);
		List<EmployeeActivityOut> employeeDailyActivities = employeeDailyActivitiesRepository
				.findEmployeeAttendance(activityList, empNo, Converter.convertLocalDateToDate(yearMonth.atDay(1)),
						Converter.convertLocalDateToDate(yearMonth.atEndOfMonth()))
				.stream().map(this::createEmployeeActivityOutFromEmployeeDailyActivities).collect(Collectors.toList());
		return employeeDailyActivities.stream()
				.collect(Collectors.groupingBy(x -> Converter.convertDateToStartOfDay(x.getActivityTime())));
	}

	@Override
	public Map<Date, List<EmployeeActivityOut>> findEmployeeAllMonthlyActivities(String empNo, Month month, int year) {

		YearMonth yearMonth = YearMonth.of(year, month);
		List<EmployeeActivityOut> employeeDailyActivities = employeeDailyActivitiesRepository
				.findEmployeeAllMonthlyActivities(empNo, Converter.convertLocalDateToDate(yearMonth.atDay(1)),
						Converter.convertLocalDateToDate(yearMonth.atEndOfMonth()))
				.stream().map(this::createEmployeeActivityOutFromEmployeeDailyActivities).collect(Collectors.toList());
		return employeeDailyActivities.stream()
				.collect(Collectors.groupingBy(x -> Converter.convertDateToStartOfDay(x.getActivityTime())));
	}

	@Override
	public List<EmployeeActivityOut> fetchEmployeeTodayActivity(String empNo) {
		return employeeDailyActivitiesRepository.findEmployeeTodayActivities(empNo).stream()
				.map(this::createEmployeeActivityOutFromEmployeeDailyActivities).collect(Collectors.toList());
	}

	@Override
	public List<EmployeeActivityOut> fetchAllAvailableEmployeesMatchingSkills(String customerService) {
		List<EmployeeDailyActivities> activeEmployees = employeeDailyActivitiesRepository
				.findAllEmployeesActivitiesToday();
		List<EmployeeActivityOut> employeeActivityOutList = new ArrayList<>();
		Map<Employee, List<EmployeeDailyActivities>> employeeByActivities = activeEmployees.stream()
				.collect(Collectors.groupingBy(EmployeeDailyActivities::getEmployee));

		List<String> employeeUsernames = employeeByActivities.keySet().stream().map(Employee::getUsername)
				.collect(Collectors.toList());

		employeeServicesRepository.findAllEmployeesMappedToService(employeeUsernames, customerService)
				.forEach(employee -> {

					List<EmployeeDailyActivities> listEmplActivities = employeeByActivities.get(employee);
					Optional<Date> employeeIn = listEmplActivities.stream()
							.filter(x -> x.getActivity() == Activity.SALON_IN).map(EmployeeDailyActivities::getTime)
							.max(Comparator.comparing(Function.identity()));
					Optional<Date> employeeOut = listEmplActivities.stream()
							.filter(x -> x.getActivity() == Activity.SALON_OUT).map(EmployeeDailyActivities::getTime)
							.max(Comparator.comparing(Function.identity()));
					if ((!employeeOut.isPresent() && employeeIn.isPresent())
							|| (employeeOut.get().getTime() < employeeIn.get().getTime())) {

						EmployeeDailyActivities employeeLastActivity = listEmplActivities.stream()
								.max(Comparator.comparing(EmployeeDailyActivities::getTime)).orElse(null);
						if (null != employeeLastActivity) {
							EmployeeActivityOut employeeActivityOut = createEmployeeActivityOutFromEmployeeDailyActivities(
									employee, employeeLastActivity);
							employeeActivityOutList.add(employeeActivityOut);
						}
					}

				});

		return employeeActivityOutList;
	}

	private EmployeeActivityOut createEmployeeActivityOutFromEmployeeDailyActivities(
			EmployeeDailyActivities employeeLastActivity) {
		return createEmployeeActivityOutFromEmployeeDailyActivities(employeeLastActivity.getEmployee(),
				employeeLastActivity);
	}

	private EmployeeActivityOut createEmployeeActivityOutFromEmployeeDailyActivities(Employee employee,
			EmployeeDailyActivities employeeLastActivity) {

		EmployeeRoster employeeSchedule = employee.getSchedule();
		Roster roster = new Roster(employeeSchedule.getInTime(), employeeSchedule.getOutTime(),
				employeeSchedule.getWeekStartDay(), employeeSchedule.getWeekEndDay());

		EmployeeActivityOut employeeActivityOut = new EmployeeActivityOut(employee.getFname(), roster,
				employee.isOvertimeWorker(), employee.getPrimaryContact(), employeeLastActivity.getActivity(),
				employeeLastActivity.getTime());
		employeeActivityOut.setUsername(employee.getUsername());
		employeeActivityOut.setmName(employee.getMname());
		employeeActivityOut.setlName(employee.getLname());
		if (null != employeeLastActivity.getCustomer()) {
			employeeActivityOut.setCustomerUsername(employeeLastActivity.getCustomer().getUsername());
			employeeActivityOut.setCustomerName(employeeLastActivity.getCustomer().getName());
		}

		return employeeActivityOut;
	}

	@Override
	public Boolean checkUsernameAvailability(String username) {
		return !employeeRepository.existsByUsername(username);
	}

	@Override
	public List<EmployeeDTO> findAllMatchingName(String name) {
		if (StringUtils.isEmpty(name) || name.trim().length() < minFNameLength) {
			throw new RequiredLengthException("Name", minFNameLength, name);
		}
		name = name.trim();
		if (name.contains(" ")) {
			/**
			 * Trim and split on space and find the customers matching
			 */
			List<String> nameArr = CustomerAndEmployeeUtils.getSplittedNameArr(name);
			if (nameArr.size() > 2)
				return employeeFacadeHelper.createEmployeeDTOList(employeeRepository.findAllMatchingName(nameArr.get(0),
						nameArr.get(1), nameArr.get(2), Sort.by("fname").ascending().and(Sort.by("mname").ascending())
								.and(Sort.by("lname").ascending())));
			return employeeFacadeHelper.createEmployeeDTOList(employeeRepository.findAllMatchingFNameLName(
					nameArr.get(0), nameArr.get(1), Sort.by("fname").ascending().and(Sort.by("lname").ascending())));
		}
		return employeeFacadeHelper.createEmployeeDTOList(employeeRepository.findAllMatchingFirstName(name));
	}

}
