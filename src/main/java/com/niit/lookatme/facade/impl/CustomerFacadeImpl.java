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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dao.CustomerJobCard;
import com.niit.lookatme.customer.dao.CustomerJobCardDetails;
import com.niit.lookatme.customer.dao.CustomerJobCardDetailsHistory;
import com.niit.lookatme.customer.dao.CustomerJobCardHistory;
import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.customer.dao.CustomerOrderHistory;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
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

	@Override
	public String createNewCustomer(CustomerInput customerInput) {
		Customer customer = customerRepository.save(createCustomerJPAFromCustomerInput(customerInput));
		if (customer.getId() != null) {
			customer.setPictureUrl(setImageUrl(UserImageInputType.PROFILE, customerInput));

			customer.setGovtIdSnapUrl(setImageUrl(UserImageInputType.GOVTID, customerInput));

			if (!StringUtils.isEmpty(customer.getPictureUrl()) || !StringUtils.isEmpty(customer.getGovtIdSnapUrl()))
				customerRepository.save(customer);
			return customer.getUsername();
		}
		return StringUtils.EMPTY;
	}

	private Customer createCustomerJPAFromCustomerInput(CustomerInput customerInput) {
		Customer customer = new Customer();
		customer.setFname(customerInput.getfName());
		customer.setMname(customerInput.getmName());
		customer.setLname(customerInput.getlName());
		customer.setDob(customerInput.getDob());

		if (StringUtils.isEmpty(customerInput.getUsername()))
			customerInput.setUsername(createCustomerUsername(customerInput));
		customer.setUsername(customerInput.getUsername());

		Password password = new Password();
		password.setPassword(CustomerAndEmployeeUtils.encrypt(customerInput.getPassword()));

		customer.setPassword(password);
		customer.setEmail(customerInput.getEmail());
		customer.setGender(Gender.valueOf(customerInput.getGender()));

		customer.setBillingAddress(CustomerAndEmployeeUtils.populateAddressObject(customerInput.getBillingAddress()));
		customer.setShippingAddress(customerInput.isSameShipping() ? customer.getBillingAddress()
				: CustomerAndEmployeeUtils.populateAddressObject(customerInput.getShippingAddress()));
		customer.setContact(customerInput.getContact());
		customer.setAlternateContact(customerInput.getAlternateContact());

		GovtIdType govtIdType = new GovtIdType();
		govtIdType.setTypeName(customerInput.getGovtIdType());
		customer.setGovtIdType(govtIdType);
		customer.setGovtId(customerInput.getGovtId());
		return customer;
	}

	private String setImageUrl(UserImageInputType userImageInputType, CustomerInput customerInput) {
		if (Optional.ofNullable(customerInput.getPictureFile()).map(MultipartFile::getSize)
				.map(x -> Boolean.valueOf(x > 0)).orElse(false)) {
			String filePathName = CustomerAndEmployeeUtils.uploadPictureImage(UserType.CUSTOMER,
					customerInput.getPictureFile(), customerInput.getUsername(), userImageInputType);
			if (!StringUtils.isEmpty(filePathName)) {
				return filePathName;
			}
		}
		return null;
	}

	private String createCustomerUsername(CustomerInput customer) {
		long customerCount = customerRepository.count();
		String initString = customer.getfName().substring(0, 3)
				+ (customer.getmName().isEmpty() ? "0" : customer.getmName().substring(0, 1))
				+ customer.getlName().substring(0, 3);
		initString = StringUtils.rightPad(initString, 13, '0');
		return StringUtils.rightPad(initString, 13, String.valueOf(customerCount));
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
		customerOrder.getCustomerJobCards().addAll(createCustomerJobCard(jobStatus, enquiryInput, customerOrder));
		CustomerOrder customerOrderPersist = customerOrderRepository.save(customerOrder);
		return (!StringUtils.isEmpty(customerOrderPersist.getRequestId())) ? customerOrderPersist.getRequestId()
				: StringUtils.EMPTY;
	}

	private List<CustomerJobCard> createCustomerJobCard(JobStatus jobStatus, CreateCustomerOrderInput enquiryInput,
			CustomerOrder customerOrder) {
		List<CustomerJobCard> customerJobCardList = new ArrayList<>();
		int counter = 0;
		enquiryInput.getCreateNewServicesMap().entrySet().forEach(x -> {
			CustomerJobCard customerJobCard = new CustomerJobCard();
			customerJobCard.setJobId(CustomerAndEmployeeUtils.createRegId(jobStatus.toString(),
					enquiryInput.getUsername(), "_", StringUtils.leftPad(String.valueOf(counter), 2)));
			customerJobCard.setJobStatus(jobStatus);
			customerJobCard.setJobStartTime(x.getKey());
			customerJobCard.setCustomerOrder(customerOrder);
			for (int i = 0; i < x.getValue().size(); i++) {
				String name = x.getValue().get(i);
				com.niit.lookatme.services.dao.Service service = serviceRepository.findByName(name);

				CustomerJobCardDetails customerJobCardDetails = new CustomerJobCardDetails();
				customerJobCardDetails.setService(service);
				customerJobCardDetails.setJobStatus(jobStatus);
				customerJobCardDetails.setSubJobId(CustomerAndEmployeeUtils.createRegId(jobStatus.toString(),
						enquiryInput.getUsername(), "_", StringUtils.leftPad(String.valueOf(i), 2)));
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
		if (customerOrder == null) {
			throw new NoSuchElementException("Order doesn't exist or is already completed or cancelled");
		}

		if (customerOrder.getRequestStatus() == JobStatus.ENQUIRY
				|| customerOrder.getRequestStatus() == JobStatus.PENDING) {
			completeCustomerOrderByGivenJobStatus(customerOrder, JobStatus.CANCELLED);
			return true;
		}

		String completedJobs = customerOrder.getCustomerJobCards().stream()
				.filter(x -> x.getJobStatus() == JobStatus.COMPLETED).map(CustomerJobCard::getJobId)
				.collect(Collectors.joining());
		if (!StringUtils.isEmpty(completedJobs)) {
			throw new IllegalStateException("Order cannot be cancelled : job cards with IDs : " + completedJobs
					+ " are already completed. Please cancel all the jobs to cancel the order.");
		}

		Map<String, List<CustomerJobCardDetails>> customerOpenJobCards = customerOrder.getCustomerJobCards().stream()
				.filter(x -> x.getJobStatus() == JobStatus.INPROGRESS)
				.collect(Collectors.toMap(CustomerJobCard::getJobId, CustomerJobCard::getCustomerJobCardDetails));

		if (!customerOpenJobCards.isEmpty()) {
			StringBuilder strBuilder = new StringBuilder(
					"Order cannot be cancelled : job cards with IDs having services are already completed or in progress: ");
			customerOpenJobCards.entrySet().forEach(x -> {
				String services = x.getValue().stream().filter(
						y -> (y.getJobStatus() == JobStatus.COMPLETED || y.getJobStatus() == JobStatus.INPROGRESS))
						.map(CustomerJobCardDetails::getService).map(com.niit.lookatme.services.dao.Service::getName)
						.collect(Collectors.joining());
				strBuilder.append(x.getKey()).append(" : ").append(services).append("\n");
			});
			strBuilder.append("Please cancel these services and then cancel the order");
			throw new IllegalStateException(strBuilder.toString());
		}
		completeCustomerOrderByGivenJobStatus(customerOrder, JobStatus.CANCELLED);
		return true;
	}

	private void completeCustomerOrderByGivenJobStatus(CustomerOrder customerOrder, JobStatus updatedJobStatus) {
		CustomerOrderHistory customerOrderHistory = createCustomerOrderHistoryAllServices(customerOrder);
		customerOrderHistoryRepository.save(customerOrderHistory);
		Date currentDate = Calendar.getInstance().getTime();
		customerOrder.setRequestStatus(updatedJobStatus);
		customerOrder.setLastModifiedDate(currentDate);
		for(CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {
			customerJobCard.setJobStatus(updatedJobStatus);
			customerJobCard.setJobEndTime(currentDate);
			customerJobCard.setLastModifiedDate(currentDate);
			for(CustomerJobCardDetails customerJobCardDetails : customerJobCard.getCustomerJobCardDetails()) {
				customerJobCardDetails.setJobStatus(updatedJobStatus);
				customerJobCardDetails.setJobEndTime(currentDate);
				customerJobCardDetails.setLastModifiedDate(currentDate);
			}
		}
		customerOrderRepository.save(customerOrder);
	}
	
	

	// For every customerOrder, CustomerJobCard, CustomerJobCardDetails, do separate
	// operation.
	@Override
	public Boolean initiateEnquiryToOrder(UpdateCustomerOrderInput customerOrderInput) {
		CustomerOrder customerOrder = customerOrderRepository
				.findEnquiryByRequestId(customerOrderInput.getCustomerOrderRequestId());
		if (customerOrder == null)
			return false;
		CustomerOrderHistory customerOrderHistory = createCustomerOrderHistoryForCustomerOrderForGivenServices(
				customerOrderInput, customerOrder);

		CustomerOrderHistory customerOrderHistorySave = customerOrderHistoryRepository.save(customerOrderHistory);
		JobStatus newJobStatus = (customerOrderInput.getInitiateJobs().isEmpty()) ? JobStatus.PENDING
				: JobStatus.INPROGRESS;
		if (customerOrderHistorySave.getId() != null) {
			customerOrder.setRequestStatus(newJobStatus);

			Set<String> initiateJobsKeySet = customerOrderInput.getInitiateJobs().keySet();
			if (!initiateJobsKeySet.isEmpty()) {
				for (CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {

					if (initiateJobsKeySet.contains(customerJobCard.getJobId())) {
						customerJobCard.setJobStatus(newJobStatus);
						Map<String, CustomerJobsInput> subJobIds = customerOrderInput.getInitiateJobs()
								.get(customerJobCard.getJobId()).stream()
								.collect(Collectors.toMap(CustomerJobsInput::getSubJobId, Function.identity()));
						if (!subJobIds.isEmpty()) {
							customerJobCard.setJobStartTime(subJobIds.values().stream().map(CustomerJobsInput::getDate)
									.min(Comparator.comparing(Date::getTime)).orElse(null));

							for (CustomerJobCardDetails customerJobCardDetails : customerJobCard
									.getCustomerJobCardDetails()) {
								if (subJobIds.keySet().contains(customerJobCardDetails.getSubJobId())) {
									customerJobCardDetails.setJobStatus(newJobStatus);
									CustomerJobsInput customerJobsInput = subJobIds
											.get(customerJobCardDetails.getSubJobId());
									customerJobCardDetails.setActivityEmployee(
											employeeRepository.findByUsername(customerJobsInput.getEmployeeUsername()));
									customerJobCardDetails.setJobStartTime(customerJobsInput.getDate());
								}
							}
						}
					}

				}
				customerOrderRepository.save(customerOrder);
			}

		}
		return true;
	}
	
	private CustomerOrderHistory createCustomerOrderHistoryAllServices(CustomerOrder customerOrder) {
		CustomerOrderHistory customerOrderHistory = new CustomerOrderHistory();
		customerOrderHistory.setRequestId(customerOrder.getRequestId());
		customerOrderHistory.setRequestStatus(customerOrder.getRequestStatus());
		customerOrderHistory.setCustomer(customerOrder.getCustomer());

		for (CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {

			CustomerJobCardHistory customerJobCardHistory = new CustomerJobCardHistory();
			customerJobCardHistory.setJobId(customerJobCard.getJobId());
			customerJobCardHistory.setCustomerOrderHistory(customerOrderHistory);
			customerJobCardHistory.setJobStatus(customerJobCard.getJobStatus());
			customerJobCardHistory.setCustomerFeedback(customerJobCard.getCustomerFeedback());
			customerJobCardHistory.setJobStartTime(customerJobCard.getJobStartTime());
			customerJobCardHistory.setJobEndTime(customerJobCard.getJobEndTime());
			customerJobCardHistory.setPaymentMode(customerJobCard.getPaymentMode());
			customerJobCardHistory.setPaymentAmount(customerJobCard.getPaymentAmount());
			customerJobCardHistory.setPaidAmount(customerJobCard.getPaidAmount());
			customerJobCardHistory.setPaymentComments(customerJobCard.getPaymentComments());
			customerJobCardHistory.setInvoiceUrl(customerJobCard.getInvoiceUrl());

			List<CustomerJobCardDetailsHistory> customerJobCardDetailsList = new ArrayList<>();
			for (CustomerJobCardDetails customerJobCardDetail : customerJobCard.getCustomerJobCardDetails()) {
				CustomerJobCardDetailsHistory customerJobCardDetailsHistory = new CustomerJobCardDetailsHistory();
				customerJobCardDetailsHistory.setJobId(customerJobCardHistory);
				customerJobCardDetailsHistory.setSubJobId(customerJobCardDetail.getSubJobId());
				customerJobCardDetailsHistory.setJobStatus(customerJobCardDetail.getJobStatus());
				customerJobCardDetailsHistory.setActivityEmployee(customerJobCardDetail.getActivityEmployee());
				customerJobCardDetailsHistory.setService(customerJobCardDetail.getService());
				customerJobCardDetailsHistory.setCustomerFeedback(customerJobCardDetail.getCustomerFeedback());
				customerJobCardDetailsHistory.setJobStartTime(customerJobCardDetail.getJobStartTime());
				customerJobCardDetailsHistory.setJobEndTime(customerJobCardDetail.getJobEndTime());
				customerJobCardDetailsList.add(customerJobCardDetailsHistory);
			}
			customerJobCardHistory.setCustomerJobCardDetailsHistory(customerJobCardDetailsList);
			customerOrderHistory.getCustomerJobCards().add(customerJobCardHistory);
		}
		return customerOrderHistory;
	}

	private CustomerOrderHistory createCustomerOrderHistoryForCustomerOrderForGivenServices(
			UpdateCustomerOrderInput customerOrderInput, CustomerOrder customerOrder) {
		CustomerOrderHistory customerOrderHistory = new CustomerOrderHistory();
		customerOrderHistory.setRequestId(customerOrder.getRequestId());
		customerOrderHistory.setRequestStatus(customerOrder.getRequestStatus());
		customerOrderHistory.setCustomer(customerOrder.getCustomer());

		Map<String, CustomerJobCard> customerJobCardsMap = customerOrder.getCustomerJobCards().stream()
				.filter(x -> x.getJobStatus() == JobStatus.ENQUIRY)
				.collect(Collectors.toMap(CustomerJobCard::getJobId, Function.identity()));

		customerOrderInput.getInitiateJobs().entrySet().forEach(jobIdEntry -> {
			CustomerJobCard customerJobCard = customerJobCardsMap.get(jobIdEntry.getKey());

			CustomerJobCardHistory customerJobCardHistory = new CustomerJobCardHistory();
			customerJobCardHistory.setJobId(customerJobCard.getJobId());
			customerJobCardHistory.setCustomerOrderHistory(customerOrderHistory);
			customerJobCardHistory.setJobStatus(customerJobCard.getJobStatus());
			customerJobCardHistory.setCustomerFeedback(customerJobCard.getCustomerFeedback());
			customerJobCardHistory.setJobStartTime(customerJobCard.getJobStartTime());
			customerJobCardHistory.setJobEndTime(customerJobCard.getJobEndTime());
			customerJobCardHistory.setPaymentMode(customerJobCard.getPaymentMode());
			customerJobCardHistory.setPaymentAmount(customerJobCard.getPaymentAmount());
			customerJobCardHistory.setPaidAmount(customerJobCard.getPaidAmount());
			customerJobCardHistory.setPaymentComments(customerJobCard.getPaymentComments());
			customerJobCardHistory.setInvoiceUrl(customerJobCard.getInvoiceUrl());

			List<String> subJobIds = jobIdEntry.getValue().stream().map(CustomerJobsInput::getSubJobId)
					.collect(Collectors.toList());
			List<CustomerJobCardDetailsHistory> customerJobCardDetailsList = new ArrayList<>();
			customerJobCard.getCustomerJobCardDetails().forEach(customerJobCardDetail -> {
				if (subJobIds.contains(customerJobCardDetail.getSubJobId())) {
					CustomerJobCardDetailsHistory customerJobCardDetailsHistory = new CustomerJobCardDetailsHistory();
					customerJobCardDetailsHistory.setJobId(customerJobCardHistory);
					customerJobCardDetailsHistory.setSubJobId(customerJobCardDetail.getSubJobId());
					customerJobCardDetailsHistory.setJobStatus(customerJobCardDetail.getJobStatus());
					customerJobCardDetailsHistory.setActivityEmployee(customerJobCardDetail.getActivityEmployee());
					customerJobCardDetailsHistory.setService(customerJobCardDetail.getService());
					customerJobCardDetailsHistory.setCustomerFeedback(customerJobCardDetail.getCustomerFeedback());
					customerJobCardDetailsHistory.setJobStartTime(customerJobCardDetail.getJobStartTime());
					customerJobCardDetailsHistory.setJobEndTime(customerJobCardDetail.getJobEndTime());
					customerJobCardDetailsList.add(customerJobCardDetailsHistory);
				}
			});
			customerJobCardHistory.setCustomerJobCardDetailsHistory(customerJobCardDetailsList);
			customerOrderHistory.getCustomerJobCards().add(customerJobCardHistory);
		});
		return customerOrderHistory;
	}

}
