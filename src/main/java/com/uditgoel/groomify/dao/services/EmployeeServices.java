package com.uditgoel.groomify.dao.services;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.uditgoel.groomify.dao.employee.Employee;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "EMPLOYEE_SERVICE")
public class EmployeeServices {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_SERVICE_ID", nullable=false, updatable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICE", referencedColumnName = "SERVICE_ID")
	private Service service;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE", referencedColumnName = "EMPLOYEE_ID")
	private Employee employee;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Long getId() {
		return id;
	}

}
