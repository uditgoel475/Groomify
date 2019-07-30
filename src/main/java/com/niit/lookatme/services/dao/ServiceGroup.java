package com.niit.lookatme.services.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="SERVICE_GROUP")
public class ServiceGroup  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7116571054509080420L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SERVICE_GROUP_ID", nullable=false, updatable = false)
	private Long id;
	
	@Column(name = "NAME", unique = true, nullable = false)
	private String name;		

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
