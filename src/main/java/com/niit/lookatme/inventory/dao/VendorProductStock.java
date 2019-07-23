package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.niit.lookatme.dao.AuditInfo;

/**
 * 
 * @author Konika
 *
 */

@Entity
@Table(name = "VENDOR_PRODUCT_STOCK")
public class VendorProductStock extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6299543768857393502L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "EMPLOYEE_ID", updatable = false, nullable = false)
	private Long id;
	
	private Vendor vendor;
	
	private Product product;
	
	private int productStock;
	
	private int maxAllowedProduct;

	private double shippingCharges;
	
	private double deliveryCharges;
	
	private String specialNote;
	
	private String warehouseLocation;
	
	private float productDiscount;
	
	private boolean isProductAlive;
	
}
