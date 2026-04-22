package com.uditgoel.groomify.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uditgoel.groomify.dao.customer.Customer;
import com.uditgoel.groomify.dao.repository.CustomerRepository;
import com.uditgoel.groomify.exception.ResourceNotFoundException;

@Service("customerDetailsService")
public class CustomCustomerDetailsService implements UserDetailsService {

	@Resource
	private CustomerRepository customerRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		Customer customer = customerRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Customer not found with username : " + username));

		return UserPrincipal.createCustomer(customer);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));

		return UserPrincipal.createCustomer(customer);
	}
}