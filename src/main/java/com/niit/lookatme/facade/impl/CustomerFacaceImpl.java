package com.niit.lookatme.facade.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.Gender;
import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.Password;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dto.UserImageInputType;
import com.niit.lookatme.dto.UserType;
import com.niit.lookatme.employee.dto.CustomerInput;
import com.niit.lookatme.facade.CustomerFacade;
import com.niit.lookatme.utils.CustomerAndEmployeeUtils;

@Service("customerFacade")
public class CustomerFacaceImpl implements CustomerFacade {

	@Resource
	private CustomerRepository customerRepository;
	
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
		password.setPassword1(customerInput.getPassword());
		password.setPwdCreationDate( Date.from(LocalDate.now().atStartOfDay()
				.atZone(ZoneId.systemDefault()).toInstant()));
		customer.setPassword(password);
		customer.setEmail(customerInput.getEmail());
		customer.setGender(Gender.valueOf(customerInput.getGender()));
	
		customer.setBillingAddress(CustomerAndEmployeeUtils.populateAddressObject(customerInput.getBillingAddress()));
		customer.setShippingAddress(customerInput.isSameShipping()?customer.getBillingAddress():CustomerAndEmployeeUtils.populateAddressObject(customerInput.getShippingAddress()));
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
					customerInput.getPictureFile(), customerInput.getUsername(),
					userImageInputType);
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
		
		return customerRepository.findAllByDobBetweenOrderByDobAsc(startDate,
				endDate);
	}

}
