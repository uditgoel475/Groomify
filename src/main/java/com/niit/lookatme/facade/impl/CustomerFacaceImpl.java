package com.niit.lookatme.facade.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.facade.CustomerFacade;

@Service("customerFacade")
public class CustomerFacaceImpl implements CustomerFacade {

	@Resource
	private CustomerRepository customerRepository;
	
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
	
	private String createCustomerUsername(Customer customer) {
		long customerCount = customerRepository.count();
		String initString = customer.getFname().substring(0, 3)
				+ (customer.getMname().isEmpty() ? "0" : customer.getMname().substring(0, 1))
				+ customer.getLname().substring(0, 3);
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
