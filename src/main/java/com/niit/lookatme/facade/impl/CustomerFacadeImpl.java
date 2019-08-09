package com.niit.lookatme.facade.impl;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dao.CustomerJobCard;
import com.niit.lookatme.customer.dao.CustomerJobCardDetails;
import com.niit.lookatme.customer.dao.CustomerJobCardDetailsHistory;
import com.niit.lookatme.customer.dao.CustomerJobCardHistory;
import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.customer.dao.CustomerOrderHistory;
import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.dao.Password;
import com.niit.lookatme.dao.repository.CustomerOrderHistoryRepository;
import com.niit.lookatme.dao.repository.CustomerOrderRepository;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.dao.repository.ServiceRepository;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dto.CreateCustomerOrderInput;
import com.niit.lookatme.employee.dto.CustomerInput;
import com.niit.lookatme.employee.dto.CustomerJobsInput;
import com.niit.lookatme.employee.dto.UpdateCustomerOrderInput;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.facade.helper.CustomerFacadeHelper;
import com.niit.lookatme.utils.AppUtils;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Service("customerFacade")
public class CustomerFacadeImpl implements CustomerFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFacadeImpl.class);

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private CustomerOrderRepository customerOrderRepository;

	@Resource
	private ServiceRepository serviceRepository;

	@Resource
	private CustomerOrderHistoryRepository customerOrderHistoryRepository;

	@Resource
	private EmployeeRepository employeeRepository;
	
	@Resource(name = "customerFacadeHelper")
	private CustomerFacadeHelper customerFacadeHelper;

	@Override
	public String createNewCustomer(CustomerInput customerInput) {
		Customer customer = customerRepository.save(customerFacadeHelper.createCustomerJPAFromCustomerInput(customerInput));
		if (customer.getId() != null) {
			customer.setPictureUrl(CustomerAndEmployeeUtils.createImageAndFetchUrl(UserType.CUSTOMER, UserImageInputType.PROFILE,
					customerInput.getPictureFile(), customerInput.getUsername()));

			customer.setGovtIdSnapUrl(CustomerAndEmployeeUtils.createImageAndFetchUrl(UserType.CUSTOMER, UserImageInputType.GOVTID,
					customerInput.getPictureFile(), customerInput.getUsername()));

			if (!StringUtils.isEmpty(customer.getPictureUrl()) || !StringUtils.isEmpty(customer.getGovtIdSnapUrl()))
				customerRepository.save(customer);
			return customer.getUsername();
		}
		return StringUtils.EMPTY;
	}

	@Override
	public List<Customer> fetchAllCustomerCurrentWeekBirthdays() {
		Date startDate = Date.from((LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)).atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant());

		return customerRepository.findAllByDobBetweenOrderByDobAsc(startDate, endDate);
	}

	@Override
	public Boolean changeCustomerPassword(String custNo, String encryptedPassword) {
		String decryptedPassword = CustomerAndEmployeeUtils.decrypt(encryptedPassword);
		Customer customer = customerRepository.findByUsername(custNo);
		Password passwords = customer.getPassword();
		if (passwords.isMatchesPreviousPasswords(decryptedPassword))
			return false;
		passwords.setPassword(decryptedPassword);
		customer.setPassword(passwords);
		try {
			customerRepository.save(customer);
			return true;
		} catch (DataAccessException | HibernateException ex) {
			LOGGER.error(ex.getMessage());
			return false;
		}
	}

	@Override
	public List<CustomerOrder> fetchAllOpenCustomerOrder(String username) {
		List<JobStatus> jobStatusList = new ArrayList<>();
		jobStatusList.add(JobStatus.PENDING);
		jobStatusList.add(JobStatus.INPROGRESS);
		return customerOrderRepository.findAllByRequestStatusInAndCustomer_Username(jobStatusList, username);
	}

	@Override
	public List<CustomerOrder> fetchAllCalendarOpenAppointmentCurrentMonth(int year, String month) {
		LocalDate localDate = LocalDate.now().withYear(year).withMonth(Month.valueOf(month.toUpperCase()).getValue());
		Date date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		return customerOrderRepository.findAllCalendarMonthOpenAppointment(date);
	}

	@Override
	public List<CustomerOrder> fetchAllCustomerCalendarOpenAppointmentGivenDate(String custNo, Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		LocalDate localDate = localDateTime.toLocalDate();
		Date searchDate = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		return customerOrderRepository.findCustomerCalendarOpenAppointmentGivenDate(custNo, searchDate);
	}

	@Override
	public List<CustomerOrder> fetchAllCustomerEnquiryGivenDate(String custNo, Date date) {
		return customerOrderRepository.findAllCustomerEnquiryGivenDate(custNo, AppUtils.convertDateToStartOfDay(date));
	}

	@Override
	public List<CustomerOrder> fetchAllCustomerEnquiries(String custNo) {
		return customerOrderRepository.fetchAllCustomerEnquiries(custNo);
	}

	@Override
	public List<CustomerOrder> fetchAllEnquiriesGivenDate(Date date) {
		return customerOrderRepository.fetchAllEnquiriesGivenDate(date);
	}

	@Override
	public String createNewCustomerEnquiry(CreateCustomerOrderInput enquiryInput) {

		return createNewCustomerOrder(JobStatus.ENQUIRY, enquiryInput);
	}

	private String createNewCustomerOrder(JobStatus jobStatus, CreateCustomerOrderInput enquiryInput) {
		CustomerOrder customerOrder = new CustomerOrder();
		customerOrder
				.setRequestId(CustomerAndEmployeeUtils.createRegId(jobStatus.toString(), enquiryInput.getUsername()));
		customerOrder.setRequestStatus(jobStatus);
		customerOrder.setCustomer(customerRepository.findByUsername(enquiryInput.getUsername()));
		customerOrder.setAppointmentDate(enquiryInput.getAppointmentDate());
		customerOrder.getCustomerJobCards().addAll(createMultipleJobCardsNew(jobStatus, enquiryInput, customerOrder));
		CustomerOrder customerOrderPersist = customerOrderRepository.save(customerOrder);
		return (!StringUtils.isEmpty(customerOrderPersist.getRequestId())) ? customerOrderPersist.getRequestId()
				: StringUtils.EMPTY;
	}

	private List<CustomerJobCard> createMultipleJobCardsNew(JobStatus jobStatus, CreateCustomerOrderInput createCustomerOrderInput,
			CustomerOrder customerOrder) {
		List<CustomerJobCard> customerJobCardList = new ArrayList<>();
		int counter = 0;
		createCustomerOrderInput.getCreateNewServicesMap().entrySet().forEach(serviceDateNameEntry -> {
			CustomerJobCard customerJobCard = new CustomerJobCard();
			customerJobCard.setJobId(CustomerAndEmployeeUtils.createRegId(jobStatus.toString(),
					createCustomerOrderInput.getUsername(), "_", StringUtils.leftPad(String.valueOf(counter), 2)));
			customerJobCard.setJobStatus(jobStatus);
			customerJobCard.setJobStartTime(serviceDateNameEntry.getKey());
			customerJobCard.setCustomerOrder(customerOrder);
			for (int i = 0; i < serviceDateNameEntry.getValue().size(); i++) {
				String serviceName = serviceDateNameEntry.getValue().get(i);
				com.niit.lookatme.services.dao.Service service = serviceRepository.findByName(serviceName);

				CustomerJobCardDetails customerJobCardDetails = new CustomerJobCardDetails();
				customerJobCardDetails.setService(service);
				customerJobCardDetails.setJobStatus(jobStatus);
				customerJobCardDetails.setSubJobId(CustomerAndEmployeeUtils.createRegId(jobStatus.toString(),
						createCustomerOrderInput.getUsername(), "_", StringUtils.leftPad(String.valueOf(i), 2)));
				customerJobCardDetails.setJobId(customerJobCard);
				customerJobCard.getCustomerJobCardDetails().add(customerJobCardDetails);
			}
			customerJobCardList.add(customerJobCard);
		});

		return customerJobCardList;
	}

	@Override
	public List<CustomerOrder> fetchAllCustomerEnquiriesDateRange(Date startDate, Date endDate) {
		return customerOrderRepository.findAllCustomerEnquiriesDateRange(startDate, endDate);
	}

	@Override
	public String createNewCustomerOrder(CreateCustomerOrderInput customerOrderInput) {
		return createNewCustomerOrder(JobStatus.PENDING, customerOrderInput);
	}

	@Override
	public Boolean cancelEntireOrder(String requestId) {
		CustomerOrder customerOrder = customerOrderRepository.findCancellableOrderById(requestId);
		customerFacadeHelper.validateCustomerOrderForCancellation(customerOrder);
		CustomerOrderHistory customerOrderHistory = createCustomerOrderHistoryAllServices(customerOrder);
		customerOrderHistoryRepository.save(customerOrderHistory);
		customerOrderRepository.save(customerFacadeHelper.updateCustomerOrderByGivenJobStatus(customerOrder, JobStatus.CANCELLED));
		return true;
	}

	@Override
	public Boolean updateServices(UpdateCustomerOrderInput customerOrderInput) {
		CustomerOrder customerOrder = customerOrderRepository
				.findCancellableOrderById(customerOrderInput.getCustomerOrderRequestId());
		if (customerOrder == null)
			return false;
		CustomerOrderHistory customerOrderHistory = customerOrderHistoryRepository
				.findByRequestId(customerOrder.getRequestId());
		Map<String, CustomerJobCardHistory> customerJobHistoryCards = customerOrderHistory.getCustomerJobCards()
				.stream().collect(Collectors.toMap(CustomerJobCardHistory::getJobId, Function.identity()));
		for (CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {
			CustomerJobCardHistory customerJobCardHistory = customerJobHistoryCards.get(customerJobCard.getJobId());
			if (null == customerJobCardHistory) {
				customerJobCardHistory = customerFacadeHelper.createJobCardHistoryFromJobCard(customerOrderHistory, customerJobCard);
			}

			List<CustomerJobCardDetailsHistory> customerJobCardDetailsList = new ArrayList<>();

			updateCustomerJobCardGivenServices(customerOrderInput, customerJobCard, customerJobCardHistory,
					customerJobCardDetailsList);
			customerOrderHistory.getCustomerJobCards().add(customerJobCardHistory);
		}
		customerOrderHistoryRepository.save(customerOrderHistory);
		customerOrderRepository.save(customerOrder);
		return true;
	}

	
	private void updateCustomerJobCardGivenServices(UpdateCustomerOrderInput customerOrderInput,
			CustomerJobCard customerJobCard, CustomerJobCardHistory customerJobCardHistory,
			List<CustomerJobCardDetailsHistory> customerJobCardDetailsList) {
		Map<String, CustomerJobsInput> initSubJobIds = customerOrderInput.getInitiateJobs()
				.get(customerJobCard.getJobId()).stream()
				.collect(Collectors.toMap(CustomerJobsInput::getSubJobId, Function.identity()));

		Map<String, CustomerJobsInput> endSubJobIds = customerOrderInput.getEndJobs().get(customerJobCard.getJobId())
				.stream().collect(Collectors.toMap(CustomerJobsInput::getSubJobId, Function.identity()));

		List<String> cancelSubJobIds = customerOrderInput.getCancelJobs().get(customerJobCard.getJobId());
		int cancelledJobCounter = (int) customerJobCard.getCustomerJobCardDetails().stream()
				.map(CustomerJobCardDetails::getJobStatus).filter(x -> x == JobStatus.CANCELLED).count();

		for (CustomerJobCardDetails customerJobCardDetail : customerJobCard.getCustomerJobCardDetails()) {

			if (initSubJobIds.containsKey(customerJobCardDetail.getSubJobId())
					|| endSubJobIds.containsKey(customerJobCardDetail.getSubJobId())
					|| cancelSubJobIds.contains(customerJobCardDetail.getSubJobId())) {
				customerJobCardDetailsList.add(
						customerFacadeHelper.createJobCardDetailsHistoryFromJobCardDetails(customerJobCardHistory, customerJobCardDetail));
			}

			cancelledJobCounter = cancelledJobCounter + updateJobCardDetailsGivenJobStatus(customerOrderInput, customerJobCard, initSubJobIds,
					endSubJobIds, cancelSubJobIds, customerJobCardDetail);
		}

		if (customerJobCard.getJobStatus() == JobStatus.PENDING && !initSubJobIds.isEmpty()) {
			customerJobCard.setJobStartTime(initSubJobIds.values().stream().map(CustomerJobsInput::getDate)
					.min(Comparator.comparing(Date::getTime)).orElse(null));
			customerJobCard.setJobStatus(JobStatus.INPROGRESS);
		} else if (customerJobCard.getJobStatus() == JobStatus.INPROGRESS
				&& cancelledJobCounter == customerJobCard.getCustomerJobCardDetails().size()) {
			customerJobCard.setJobEndTime(customerJobCard.getJobStartTime());
			customerJobCard.setJobStatus(JobStatus.CANCELLED);
		}
	}

	private int updateJobCardDetailsGivenJobStatus(UpdateCustomerOrderInput customerOrderInput,
			CustomerJobCard customerJobCard, Map<String, CustomerJobsInput> initSubJobIds,
			Map<String, CustomerJobsInput> endSubJobIds, List<String> cancelSubJobIds,
			CustomerJobCardDetails customerJobCardDetail) {
		int cancelledJobCounter = 0;
		if (customerOrderInput.getInitiateJobs().containsKey(customerJobCard.getJobId())
				&& initSubJobIds.containsKey(customerJobCardDetail.getSubJobId())) {

			customerJobCardDetail.setJobStatus(JobStatus.INPROGRESS);
			CustomerJobsInput customerJobsInput = initSubJobIds.get(customerJobCardDetail.getSubJobId());
			customerJobCardDetail.setActivityEmployee(
					employeeRepository.findByUsername(customerJobsInput.getEmployeeUsername()));
			customerJobCardDetail.setJobStartTime(customerJobsInput.getDate());

		} else if (customerOrderInput.getCancelJobs().containsKey(customerJobCard.getJobId())
				&& cancelSubJobIds.contains(customerJobCardDetail.getSubJobId())) {
			customerJobCardDetail.setJobEndTime(customerJobCardDetail.getJobStartTime());
			customerJobCardDetail.setJobStatus(JobStatus.CANCELLED);
			cancelledJobCounter++;

		} else if (customerJobCardDetail.getJobStatus() == JobStatus.INPROGRESS
				&& customerJobCardDetail.getJobStartTime().getTime() < Calendar.getInstance().getTimeInMillis()
				&& customerOrderInput.getEndJobs().containsKey(customerJobCard.getJobId())
				&& endSubJobIds.containsKey(customerJobCardDetail.getSubJobId())) {
			customerJobCardDetail.setJobStatus(JobStatus.COMPLETED);
			customerJobCardDetail.setJobEndTime(endSubJobIds.get(customerJobCardDetail.getSubJobId()).getDate());
		} else {
			customerJobCardDetail.setJobStatus(JobStatus.PENDING);
		}
		return cancelledJobCounter;
	}

	@Override
	public Boolean initiateEnquiryToOrder(UpdateCustomerOrderInput customerOrderInput) {
		CustomerOrder customerOrder = customerOrderRepository
				.findEnquiryByRequestId(customerOrderInput.getCustomerOrderRequestId());
		if (customerOrder == null)
			return false;

		JobStatus newJobStatus = (customerOrderInput.getInitiateJobs().isEmpty()) ? JobStatus.PENDING
				: JobStatus.INPROGRESS;
		customerOrder.setRequestStatus(newJobStatus);

		CustomerOrderHistory customerOrderHistory = new CustomerOrderHistory();
		customerOrderHistory.setRequestId(customerOrder.getRequestId());
		customerOrderHistory.setRequestStatus(customerOrder.getRequestStatus());
		customerOrderHistory.setCustomer(customerOrder.getCustomer());

		for (CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {

			CustomerJobCardHistory customerJobCardHistory = customerFacadeHelper.createJobCardHistoryFromJobCard(customerOrderHistory,
					customerJobCard);

			List<CustomerJobCardDetailsHistory> customerJobCardDetailsList = new ArrayList<>();
			updateCustomerJobCardGivenCustomerOrderInput(customerOrderInput, customerJobCard,
					customerJobCardDetailsList, customerJobCardHistory);
			customerOrderHistory.getCustomerJobCards().add(customerJobCardHistory);

		}
		customerOrderHistoryRepository.save(customerOrderHistory);
		customerOrderRepository.save(customerOrder);
		return true;
	}

	private void updateCustomerJobCardGivenCustomerOrderInput(UpdateCustomerOrderInput customerOrderInput,
			CustomerJobCard customerJobCard, List<CustomerJobCardDetailsHistory> customerJobCardDetailsList,
			CustomerJobCardHistory customerJobCardHistory) {
		Map<String, CustomerJobsInput> initSubJobIds = customerOrderInput.getInitiateJobs()
				.get(customerJobCard.getJobId()).stream()
				.collect(Collectors.toMap(CustomerJobsInput::getSubJobId, Function.identity()));

		Map<String, CustomerJobsInput> endSubJobIds = customerOrderInput.getEndJobs().get(customerJobCard.getJobId())
				.stream().collect(Collectors.toMap(CustomerJobsInput::getSubJobId, Function.identity()));

		int cancelledJobCounter = (int) customerJobCard.getCustomerJobCardDetails().stream()
				.map(CustomerJobCardDetails::getJobStatus).filter(x -> x == JobStatus.CANCELLED).count();
		List<String> cancelSubJobIds = customerOrderInput.getCancelJobs().get(customerJobCard.getJobId());
		for (CustomerJobCardDetails customerJobCardDetail : customerJobCard.getCustomerJobCardDetails()) {

			customerJobCardDetailsList.add(customerFacadeHelper.createJobCardDetailsHistoryFromJobCardDetails(customerJobCardHistory,
					customerJobCardDetail));
			
			cancelledJobCounter = cancelledJobCounter + updateJobCardDetailsGivenJobStatus(customerOrderInput, customerJobCard, initSubJobIds,
					endSubJobIds, cancelSubJobIds, customerJobCardDetail);
		
		}

		if (!initSubJobIds.isEmpty()) {
			customerJobCard.setJobStartTime(initSubJobIds.values().stream().map(CustomerJobsInput::getDate)
					.min(Comparator.comparing(Date::getTime)).orElse(null));
			customerJobCard.setJobStatus(JobStatus.INPROGRESS);
		} else if (cancelledJobCounter == customerJobCard.getCustomerJobCardDetails().size()) {
			customerJobCard.setJobStatus(JobStatus.CANCELLED);
		} else {
			customerJobCard.setJobStatus(JobStatus.PENDING);
		}
	}

	private CustomerOrderHistory createCustomerOrderHistoryAllServices(CustomerOrder customerOrder) {
		CustomerOrderHistory customerOrderHistory = new CustomerOrderHistory();
		customerOrderHistory.setRequestId(customerOrder.getRequestId());
		customerOrderHistory.setRequestStatus(customerOrder.getRequestStatus());
		customerOrderHistory.setCustomer(customerOrder.getCustomer());

		for (CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {

			CustomerJobCardHistory customerJobCardHistory = customerFacadeHelper.createJobCardHistoryFromJobCard(customerOrderHistory, customerJobCard);

			List<CustomerJobCardDetailsHistory> customerJobCardDetailsList = new ArrayList<>();
			for (CustomerJobCardDetails customerJobCardDetail : customerJobCard.getCustomerJobCardDetails()) {
				customerJobCardDetailsList.add(
						customerFacadeHelper.createJobCardDetailsHistoryFromJobCardDetails(customerJobCardHistory, customerJobCardDetail));
			}
			customerJobCardHistory.setCustomerJobCardDetailsHistory(customerJobCardDetailsList);
			customerOrderHistory.getCustomerJobCards().add(customerJobCardHistory);
		}
		return customerOrderHistory;
	}
}
