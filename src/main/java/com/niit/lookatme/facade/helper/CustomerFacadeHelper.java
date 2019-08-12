package com.niit.lookatme.facade.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dao.CustomerJobCard;
import com.niit.lookatme.customer.dao.CustomerJobCardDetails;
import com.niit.lookatme.customer.dao.CustomerJobCardDetailsHistory;
import com.niit.lookatme.customer.dao.CustomerJobCardHistory;
import com.niit.lookatme.customer.dao.CustomerOrder;
import com.niit.lookatme.customer.dao.CustomerOrderHistory;
import com.niit.lookatme.customer.dto.CustomerDTO;
import com.niit.lookatme.customer.dto.CustomerJobCardDetailsOut;
import com.niit.lookatme.customer.dto.CustomerJobCardOut;
import com.niit.lookatme.customer.dto.CustomerOrderOut;
import com.niit.lookatme.customer.dto.CustomerOutDTO;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.dao.Password;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Component("customerFacadeHelper")
public class CustomerFacadeHelper {

	@Resource
	private CustomerRepository customerRepository;

	@Resource
	private EmployeeFacadeHelper employeeFacadeHelper;

	public CustomerOrder updateCustomerOrderByGivenJobStatus(CustomerOrder customerOrder, JobStatus updatedJobStatus) {

		Date currentDate = Calendar.getInstance().getTime();
		customerOrder.setRequestStatus(updatedJobStatus);
		customerOrder.setLastModifiedDate(currentDate);
		for (CustomerJobCard customerJobCard : customerOrder.getCustomerJobCards()) {
			customerJobCard.setJobStatus(updatedJobStatus);
			customerJobCard.setJobEndTime(currentDate);
			customerJobCard.setLastModifiedDate(currentDate);
			for (CustomerJobCardDetails customerJobCardDetails : customerJobCard.getCustomerJobCardDetails()) {
				customerJobCardDetails.setJobStatus(updatedJobStatus);
				customerJobCardDetails.setJobEndTime(currentDate);
				customerJobCardDetails.setLastModifiedDate(currentDate);
			}
		}
		return customerOrder;
	}

	public void validateCustomerOrderForCancellation(CustomerOrder customerOrder) {
		if (customerOrder == null) {
			throw new NoSuchElementException("Order doesn't exist or is already completed or cancelled");
		}

		validateCustomerJobCardsForCancellation(customerOrder.getCustomerJobCards());
	}

	public void validateCustomerJobCardsForCancellation(List<CustomerJobCard> listCustomerJobCards) {
		String completedJobs = listCustomerJobCards.stream().filter(x -> x.getJobStatus() == JobStatus.COMPLETED)
				.map(CustomerJobCard::getJobId).collect(Collectors.joining());
		if (!StringUtils.isEmpty(completedJobs)) {
			throw new IllegalStateException("Job Card cannot be cancelled : Job Cards : " + completedJobs
					+ " are already completed. Please manually cancel them to cancel the Job Card.");
		}

		Map<String, List<CustomerJobCardDetails>> customerOpenJobCards = listCustomerJobCards.stream()
				.filter(x -> x.getJobStatus() == JobStatus.INPROGRESS)
				.collect(Collectors.toMap(CustomerJobCard::getJobId, CustomerJobCard::getCustomerJobCardDetails));

		if (!customerOpenJobCards.isEmpty()) {
			StringBuilder strBuilder = new StringBuilder(
					"Job Card cannot be cancelled : Job Cards have Active or Completed services: ");
			customerOpenJobCards.entrySet().forEach(x -> {
				String services = x.getValue().stream().filter(
						y -> (y.getJobStatus() == JobStatus.COMPLETED || y.getJobStatus() == JobStatus.INPROGRESS))
						.map(CustomerJobCardDetails::getService).map(com.niit.lookatme.services.dao.Service::getName)
						.collect(Collectors.joining());
				strBuilder.append(x.getKey()).append(" : ").append(services).append("\n");
			});
			strBuilder.append("Please cancel the services");
			throw new IllegalStateException(strBuilder.toString());
		}
	}

	public CustomerJobCardHistory createJobCardHistoryFromJobCard(CustomerOrderHistory customerOrderHistory,
			CustomerJobCard customerJobCard) {
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
		return customerJobCardHistory;
	}

	public CustomerJobCardDetailsHistory createJobCardDetailsHistoryFromJobCardDetails(
			CustomerJobCardHistory customerJobCardHistory, CustomerJobCardDetails customerJobCardDetail) {
		CustomerJobCardDetailsHistory customerJobCardDetailsHistory = new CustomerJobCardDetailsHistory();
		customerJobCardDetailsHistory.setJobId(customerJobCardHistory);
		customerJobCardDetailsHistory.setSubJobId(customerJobCardDetail.getSubJobId());
		customerJobCardDetailsHistory.setJobStatus(customerJobCardDetail.getJobStatus());
		customerJobCardDetailsHistory.setActivityEmployee(customerJobCardDetail.getActivityEmployee());
		customerJobCardDetailsHistory.setService(customerJobCardDetail.getService());
		customerJobCardDetailsHistory.setCustomerFeedback(customerJobCardDetail.getCustomerFeedback());
		customerJobCardDetailsHistory.setJobStartTime(customerJobCardDetail.getJobStartTime());
		customerJobCardDetailsHistory.setJobEndTime(customerJobCardDetail.getJobEndTime());
		return customerJobCardDetailsHistory;
	}

	public Customer createCustomerJPAFromCustomerInput(CustomerDTO customerInput) {
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

	private String createCustomerUsername(CustomerDTO customer) {
		long customerCount = customerRepository.count();
		String initString = customer.getfName().substring(0, 3)
				+ (customer.getmName().isEmpty() ? "0" : customer.getmName().substring(0, 1))
				+ customer.getlName().substring(0, 3);
		initString = StringUtils.rightPad(initString, 13, '0');
		return StringUtils.rightPad(initString, 13, String.valueOf(customerCount));
	}

	public CustomerDTO createCustomerDTOFromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO(customer.getUsername(), customer.getName(), customer.getDob(),
				customer.getContact(), customer.getGender().toString(), customer.getRegId(), customer.getEmail());
		customerDTO.setfName(customer.getFname());
		customerDTO.setmName(customer.getMname());
		customerDTO.setlName(customer.getLname());
		customerDTO.setAlternateContact(customer.getAlternateContact());
		return customerDTO;
	}

	public CustomerJobCardDetailsOut createCustomerJobCardDetailsOut(CustomerJobCardDetails customerJobCardDetails) {
		return new CustomerJobCardDetailsOut(customerJobCardDetails.getSubJobId(),
				customerJobCardDetails.getJobStatus(),
				employeeFacadeHelper.createEmployeeDTO(customerJobCardDetails.getActivityEmployee()),
				customerJobCardDetails.getService().getName(), customerJobCardDetails.getCustomerFeedback(),
				customerJobCardDetails.getJobStartTime(), customerJobCardDetails.getJobEndTime());
	}

	public CustomerJobCardOut createCustomerJobCardOut(CustomerJobCard customerJobCard) {
		List<CustomerJobCardDetailsOut> customerJobCardDetailsOutList = customerJobCard.getCustomerJobCardDetails()
				.stream().map(x -> createCustomerJobCardDetailsOut(x)).collect(Collectors.toList());
		CustomerJobCardOut customerJobCardOut = new CustomerJobCardOut(customerJobCard.getJobId(),
				customerJobCard.getJobStatus(), customerJobCard.getJobStartTime(), customerJobCard.getJobEndTime(),
				customerJobCard.getPaymentMode(), customerJobCard.getPaymentAmount(), customerJobCardDetailsOutList);
		customerJobCardOut.setCustomerFeedback(customerJobCard.getCustomerFeedback());
		customerJobCardOut.setPaidAmount(customerJobCard.getPaidAmount());
		customerJobCardOut.setPaymentComments(customerJobCard.getPaymentComments());
		return customerJobCardOut;
	}
	
	public CustomerOutDTO createCustomerOutDTO(Customer customer) {
		CustomerOutDTO customerOutDTO = new CustomerOutDTO(customer.getUsername(), customer.getName(),
				customer.getDob(), customer.getContact(), customer.getGender().toString(), customer.getRegId(),
				customer.getEmail());
		customerOutDTO.setAlternateContact(customer.getAlternateContact());
		customerOutDTO.setBillingAddress(CustomerAndEmployeeUtils.populateAddressOut(customer.getBillingAddress()));
		customerOutDTO.setShippingAddress(CustomerAndEmployeeUtils.populateAddressOut(customer.getShippingAddress()));
		return customerOutDTO;
	}

	public CustomerOrderOut createCustomerOrderOut(CustomerOrder customerOrder) {
		Customer customer = customerOrder.getCustomer();
		CustomerOutDTO customerOutDTO = createCustomerOutDTO(customer);

		List<CustomerJobCardOut> customerJobCardList = customerOrder.getCustomerJobCards().stream()
				.map(x -> createCustomerJobCardOut(x)).collect(Collectors.toList());
		return new CustomerOrderOut(customerOrder.getRequestId(), customerOrder.getRequestStatus(), customerOutDTO,
				customerOrder.getAppointmentDate(), customerJobCardList);
	}
}
