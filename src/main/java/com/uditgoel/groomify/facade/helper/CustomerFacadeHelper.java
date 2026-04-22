package com.uditgoel.groomify.facade.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.uditgoel.groomify.dao.Gender;
import com.uditgoel.groomify.dao.GovtIdType;
import com.uditgoel.groomify.dao.JobStatus;
import com.uditgoel.groomify.dao.Password;
import com.uditgoel.groomify.dao.customer.Customer;
import com.uditgoel.groomify.dao.customer.CustomerJobCard;
import com.uditgoel.groomify.dao.customer.CustomerJobCardDetails;
import com.uditgoel.groomify.dao.customer.CustomerJobCardDetailsHistory;
import com.uditgoel.groomify.dao.customer.CustomerJobCardHistory;
import com.uditgoel.groomify.dao.customer.CustomerOrder;
import com.uditgoel.groomify.dao.customer.CustomerOrderHistory;
import com.uditgoel.groomify.dao.repository.CustomerRepository;
import com.uditgoel.groomify.dto.UserType;
import com.uditgoel.groomify.dto.customer.CustomerDTO;
import com.uditgoel.groomify.dto.customer.CustomerJobCardDetailsOut;
import com.uditgoel.groomify.dto.customer.CustomerJobCardOut;
import com.uditgoel.groomify.dto.customer.CustomerOrderOut;
import com.uditgoel.groomify.dto.customer.CustomerOutDTO;
import com.uditgoel.groomify.utils.CustomerAndEmployeeUtils;

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
						.map(CustomerJobCardDetails::getService).map(com.uditgoel.groomify.dao.services.Service::getName)
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

		if (!StringUtils.isEmpty(customerInput.getName())) {
			List<String> splitName = CustomerAndEmployeeUtils.getSplittedNameArr(customerInput.getName());
			customer.setFname(splitName.get(0));
			if (splitName.size() > 1 && !StringUtils.isEmpty(splitName.get(1))) {
				customer.setMname(splitName.get(1));
			}
			if (splitName.size() > 2 && !StringUtils.isEmpty(splitName.get(2))) {
				customer.setLname(splitName.get(2));
			}
		}

		customer.setDob(customerInput.getDob());

		if (StringUtils.isEmpty(customerInput.getUsername()))
			customerInput.setUsername(createCustomerUsername(customerInput));
		customer.setUsername(customerInput.getUsername());

		Password password = new Password();
		password.setPassword(customerInput.getPassword(), UserType.CUSTOMER);

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

		List<String> splitName = CustomerAndEmployeeUtils.getSplittedNameArr(customer.getName());

		StringBuilder nameBuilder = new StringBuilder(splitName.get(0));
		if (splitName.size() > 1 && !StringUtils.isEmpty(splitName.get(1)))
			nameBuilder.append('.').append(splitName.get(1));
		if (splitName.size() > 2 && !StringUtils.isEmpty(splitName.get(2)))
			nameBuilder.append('.').append(splitName.get(2));
		nameBuilder.append('.');

		List<String> matchingUsername = customerRepository.findAllUsernameStartsWith(nameBuilder.toString());
		while (matchingUsername.contains(nameBuilder.toString().concat(String.valueOf(customerCount)))) {
			nameBuilder.append(0);
		}

		return nameBuilder.append(customerCount).toString();
	}

	public List<CustomerDTO> createCustomerDTOListFromCustomerList(List<Customer> customerList) {
		return customerList.stream().map(this::createCustomerDTOFromCustomer).collect(Collectors.toList());
	}

	public CustomerDTO createCustomerDTOFromCustomer(Customer customer) {
		CustomerDTO customerDTO = new CustomerDTO(customer.getUsername(), customer.getName(), customer.getDob(),
				customer.getContact(), customer.getGender().toString(), customer.getRegId(), customer.getEmail());
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
				.stream().map(this::createCustomerJobCardDetailsOut).collect(Collectors.toList());
		CustomerJobCardOut customerJobCardOut = new CustomerJobCardOut(customerJobCard.getJobId(),
				customerJobCard.getJobStatus(), customerJobCard.getJobStartTime(), customerJobCard.getJobEndTime(),
				customerJobCard.getPaymentMode(), customerJobCard.getPaymentAmount(), customerJobCardDetailsOutList);
		customerJobCardOut.setCustomerFeedback(customerJobCard.getCustomerFeedback());
		customerJobCardOut.setPaidAmount(customerJobCard.getPaidAmount());
		customerJobCardOut.setPaymentComments(customerJobCard.getPaymentComments());
		return customerJobCardOut;
	}

	public List<CustomerOutDTO> createCustomerOutDTOList(List<Customer> customerList) {
		return customerList.stream().map(this::createCustomerOutDTO).collect(Collectors.toList());
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
				.map(this::createCustomerJobCardOut).collect(Collectors.toList());
		return new CustomerOrderOut(customerOrder.getRequestId(), customerOrder.getRequestStatus(), customerOutDTO,
				customerOrder.getAppointmentDate(), customerJobCardList);
	}
}
