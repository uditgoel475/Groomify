package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.niit.lookatme.dao.AuditInfo;

@Entity
@Table(name = "VENDOR")
public class Vendor extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6539048894121395352L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_ID", nullable = false, updatable = false)
    private Long id;
	
	@Column(name = "COMPANY_NAME")
	private String companyName;
	
}
