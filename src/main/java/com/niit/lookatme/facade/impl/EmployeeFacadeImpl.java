package com.niit.lookatme.facade.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.Activity;
import com.niit.lookatme.dao.Password;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dao.repository.EmployeeDailyActivitiesRepository;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dao.Employee;
import com.niit.lookatme.employee.dao.EmployeeDailyActivities;
import com.niit.lookatme.employee.dto.EmployeeInput;
import com.niit.lookatme.facade.EmployeeFacade;
import com.niit.lookatme.facade.helper.EmployeeFacadeHelper;
import com.niit.lookatme.utils.AppUtils;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

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

		Employee employee = employeeRepository
				.save(employeeFacadeHelper.createEmployeeJPAFromEmployeeInput(createEmployeeInput));
		if (employee.getId() != null) {

			employee.setPictureUrl(
					CustomerAndEmployeeUtils.createImageAndFetchUrl(UserType.EMPLOYEE, UserImageInputType.PROFILE,
							createEmployeeInput.getPictureFile(), createEmployeeInput.getUsername()));

			employee.setGovtIdSnapUrl(
					CustomerAndEmployeeUtils.createImageAndFetchUrl(UserType.EMPLOYEE, UserImageInputType.GOVTID,
							createEmployeeInput.getPictureFile(), createEmployeeInput.getUsername()));

			if (!StringUtils.isEmpty(employee.getPictureUrl()) || !StringUtils.isEmpty(employee.getGovtIdSnapUrl()))
				employeeRepository.save(employee);
			return employee.getUsername();
		}
		return StringUtils.EMPTY;
	}

	@Override
	public Boolean changeEmployeePassword(String empNo, String encryptedPassword) {
		String decryptedPassword = CustomerAndEmployeeUtils.decrypt(encryptedPassword);
		Employee employee = employeeRepository.findByUsername(empNo);
		Password passwords = employee.getPassword();
		if (passwords.isMatchesPreviousPasswords(decryptedPassword))
			return false;
		passwords.setPassword(decryptedPassword);
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
		Employee employee = employeeRepository.findByUsername(empNo);
		employeeDailyActivities.setEmployee(employee);
		if (!StringUtils.isEmpty(custUsername)) {
			Customer customer = customerRepository.findByUsername(custUsername);
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
	public Map<Date, List<EmployeeDailyActivities>> fetchEmployeeMonthlyAttendance(String empNo, Date startDate,
			Date endDate) {
		List<Activity> activityList = new ArrayList<>();
		activityList.add(Activity.SALON_IN);
		activityList.add(Activity.SALON_OUT);
		List<EmployeeDailyActivities> employeeDailyActivities = employeeDailyActivitiesRepository
				.findEmployeeAttendance(activityList, empNo, AppUtils.convertDateToStartOfDay(startDate),
						AppUtils.convertDateToStartOfDay(endDate));
		return employeeDailyActivities.stream()
				.collect(Collectors.groupingBy(x -> AppUtils.convertDateToStartOfDay(x.getTime())));
	}

	@Override
	public Map<Date, List<EmployeeDailyActivities>> findEmployeeAllMonthlyActivities(String empNo, Date startDate,
			Date endDate) {

		List<EmployeeDailyActivities> employeeDailyActivities = employeeDailyActivitiesRepository
				.findEmployeeAllMonthlyActivities(empNo, AppUtils.convertDateToStartOfDay(startDate),
						AppUtils.convertDateToStartOfDay(endDate));
		return employeeDailyActivities.stream()
				.collect(Collectors.groupingBy(x -> AppUtils.convertDateToStartOfDay(x.getTime())));
	}

	@Override
	public List<EmployeeDailyActivities> fetchEmployeeTodayActivity(String empNo) {
		return employeeDailyActivitiesRepository.findEmployeeTodayActivities(empNo);
	}
}
