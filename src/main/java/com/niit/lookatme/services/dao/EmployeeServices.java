package com.niit.lookatme.services.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.niit.lookatme.employee.dao.Employee;

/**
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "Employee_service")
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
	private Employee salonMembers;

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Employee getSalonMembers() {
		return salonMembers;
	}

	public void setSalonMembers(Employee salonMembers) {
		this.salonMembers = salonMembers;
	}

	public Long getId() {
		return id;
	}

}
