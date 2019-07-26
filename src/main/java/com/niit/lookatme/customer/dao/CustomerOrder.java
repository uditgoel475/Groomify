package com.niit.lookatme.customer.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.niit.lookatme.dao.AuditInfo;

@Entity
@Table(name = "CUSTOMER_ORDER")
public class CustomerOrder extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6295321259673917791L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_ORDER_ID", updatable = false, nullable = false)
	private Long id;
}
