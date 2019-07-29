package com.niit.lookatme.facade;

import org.springframework.web.multipart.MultipartFile;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.customer.dto.CreateEmployeeInput;
import com.niit.lookatme.dto.CustomerAndEmployeeDataDTO;

public interface CustomerAndEmployeeFacade {

	CustomerAndEmployeeDataDTO fetchAllCustomerAndEmployeeBirthdays();

	String createNewEmployee(CreateEmployeeInput createEmployeeInput);

	String createNewCustomer(Customer customer);

	Boolean uploadPictureImage(MultipartFile file, String empNo, String typeName);
}
