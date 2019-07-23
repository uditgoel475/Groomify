package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VENDOR")
public class Vendor {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "VENDOR_ID", nullable = false, updatable = false)
    private Long id;
	
}
