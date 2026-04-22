package com.uditgoel.groomify.dao.role;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.uditgoel.groomify.dao.customer.Customer;
import com.uditgoel.groomify.dao.employee.Employee;

/**
 * @author Konika
 * 
 */
@Entity
@Table(name = "ROLES")
public class Role implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8618847665484726279L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ROLE_ID", nullable = false, updatable = false)
	private Long id;

	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60, name = "ROLE_NAME")
	private RoleName name;
	
	@ManyToMany(mappedBy = "employeeRoles")
	private Set<Employee> employees;
	
	@ManyToMany(mappedBy = "customerRoles")
	private Set<Customer> customers;

	public Role() {

	}

	public Role(RoleName name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}

	/**
	 * @return the employees
	 */
	public Set<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @return the customers
	 */
	public Set<Customer> getCustomers() {
		return customers;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(Set<Customer> customers) {
		this.customers = customers;
	}


}
