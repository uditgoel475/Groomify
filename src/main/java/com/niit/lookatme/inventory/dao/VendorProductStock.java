package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.niit.lookatme.dao.Address;
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
	@Column(name = "VENDOR_PRODUCT_STOCK_ID", updatable = false, nullable = false)
	private Long id;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT", referencedColumnName = "PRODUCT_ID")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WAREHOUSE_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address warehouseAddress;

	@Column(name = "VENDOR_PRICE", nullable = false)
	private Float vendorPrice;

	/**
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * @param vendor the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the warehouseAddress
	 */
	public Address getWarehouseAddress() {
		return warehouseAddress;
	}

	/**
	 * @param warehouseAddress the warehouseAddress to set
	 */
	public void setWarehouseAddress(Address warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	/**
	 * @return the vendorPrice
	 */
	public Float getVendorPrice() {
		return vendorPrice;
	}

	/**
	 * @param vendorPrice the vendorPrice to set
	 */
	public void setVendorPrice(Float vendorPrice) {
		this.vendorPrice = vendorPrice;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

}
