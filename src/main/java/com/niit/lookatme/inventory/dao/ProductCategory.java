package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.niit.lookatme.dao.AuditInfo;

@Entity
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory extends AuditInfo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 920523208616002004L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_CATEGORY_ID", nullable = false, updatable = false)
    private Long id;
}
