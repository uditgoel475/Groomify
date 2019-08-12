package com.niit.lookatme.security;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.niit.lookatme.dao.employee.Employee;
import com.niit.lookatme.dao.repository.EmployeeRepository;
import com.niit.lookatme.exception.ResourceNotFoundException;

@Service("customEmployeeDetailsService")
public class CustomEmployeeDetailsService implements UserDetailsService {

	@Resource
	private EmployeeRepository employeeRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) {
		Employee employee = employeeRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Employee not found with username : " + username));

		return UserPrincipal.createEmployee(employee);
	}

	@Transactional
	public UserDetails loadUserById(Long id) {
		Employee employee = employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

		return UserPrincipal.createEmployee(employee);
	}
}