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
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.JobStatus;
import com.niit.lookatme.dao.Password;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.employee.dto.CustomerDTO;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Component("customerFacadeHelper")
public class CustomerFacadeHelper {
	
	@Resource
	private CustomerRepository customerRepository;
	
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
		String completedJobs = listCustomerJobCards.stream()
				.filter(x -> x.getJobStatus() == JobStatus.COMPLETED).map(CustomerJobCard::getJobId)
				.collect(Collectors.joining());
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
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setUsername(customer.getUsername());
		customerDTO.setfName(customer.getFname());
		customerDTO.setmName(customer.getMname());
		customerDTO.setlName(customer.getLname());
		customerDTO.setDob(customer.getDob());
		customerDTO.setContact(customer.getContact());
		customerDTO.setAlternateContact(customer.getAlternateContact());
		customerDTO.setGender(customer.getGender().toString());
		customerDTO.setRegId(customer.getRegId());
		customerDTO.setEmail(customer.getEmail());
		return customerDTO;
	}
}
