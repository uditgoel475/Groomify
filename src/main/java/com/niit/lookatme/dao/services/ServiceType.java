package com.niit.lookatme.dao.services;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="SERVICE_TYPE")
public class ServiceType  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7116571054509080420L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERVICE_TYPE_ID", nullable=false, updatable = false)
	private Long id;
	
	@Column(name = "NAME", unique = true, nullable = false)
	private String name;		

	public ServiceType(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Long getId() {
		return id;
	}

}
