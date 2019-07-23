package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	@Column(name = "EMPLOYEE_ID", updatable = false, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT", referencedColumnName = "PRODUCT_ID")
	private Product product;
	
	@Column(name = "PRODUCT_STOCK", nullable=false)
	private int productStock;
	
	@Column(name = "MAX_BUY_PRODUCT")
	private int maxBuyProduct;

	@Column(name = "SHIPPING_CHARGES")
	private double shippingCharges;
	
	@Column(name = "DELIVERY_CAHRGES")
	private double deliveryCharges;
	
	@Column(name = "SPECIAL_NOTE", columnDefinition="varchar(500)")
	private String specialNote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WAREHOUSE_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address warehouseAddress;
	
	@Column(name = "PRODUCT_PRICE", nullable=false)
	private float productPrice;
	
	@Column(name = "DISCOUNT_PERCENT", nullable=false)
	private float discountPercent;
	
	@Column(name = "IS_PRODUCT_ALIVE", nullable=false, columnDefinition = "boolean default true")
	private boolean isProductAlive;

	public Long getId() {
		return id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public Product getProduct() {
		return product;
	}

	public int getProductStock() {
		return productStock;
	}

	public int getMaxBuyProduct() {
		return maxBuyProduct;
	}

	public double getShippingCharges() {
		return shippingCharges;
	}

	public double getDeliveryCharges() {
		return deliveryCharges;
	}

	public String getSpecialNote() {
		return specialNote;
	}

	public Address getWarehouseAddress() {
		return warehouseAddress;
	}

	public float getProductPrice() {
		return productPrice;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public boolean isProductAlive() {
		return isProductAlive;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}

	public void setMaxBuyProduct(int maxBuyProduct) {
		this.maxBuyProduct = maxBuyProduct;
	}

	public void setShippingCharges(double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public void setDeliveryCharges(double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public void setSpecialNote(String specialNote) {
		this.specialNote = specialNote;
	}

	public void setWarehouseAddress(Address warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	public void setProductPrice(float productPrice) {
		this.productPrice = productPrice;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public void setProductAlive(boolean isProductAlive) {
		this.isProductAlive = isProductAlive;
	}
	
}
