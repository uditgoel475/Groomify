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

import org.hibernate.annotations.ColumnDefault;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR", referencedColumnName = "VENDOR_ID")
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT", referencedColumnName = "PRODUCT_ID")
	private Product product;

	@Column(name = "PRODUCT_STOCK", nullable = false)
	private Integer productStock;

	@Column(name = "MAX_BUY_PRODUCT")
	private Integer maxBuyProduct;

	@Column(name = "SHIPPING_CHARGES")
	private Double shippingCharges;

	@Column(name = "DELIVERY_CAHRGES")
	private Double deliveryCharges;

	@Column(name = "SPECIAL_NOTE")
	private String specialNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WAREHOUSE_ADDRESS", referencedColumnName = "ADDRESS_ID")
	private Address warehouseAddress;

	@Column(name = "PRODUCT_PRICE", nullable = false)
	private Float productPrice;

	@Column(name = "DISCOUNT_PERCENT", nullable = false)
	private Float discountPercent;

	@Column(name = "IS_PRODUCT_ALIVE", nullable = false)
	@ColumnDefault("true")
	private Boolean isProductAlive;

	public Long getId() {
		return id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public Product getProduct() {
		return product;
	}

	public Integer getProductStock() {
		return productStock;
	}

	public Integer getMaxBuyProduct() {
		return maxBuyProduct;
	}

	public Double getShippingCharges() {
		return shippingCharges;
	}

	public Double getDeliveryCharges() {
		return deliveryCharges;
	}

	public String getSpecialNote() {
		return specialNote;
	}

	public Address getWarehouseAddress() {
		return warehouseAddress;
	}

	public Float getProductPrice() {
		return productPrice;
	}

	public Float getDiscountPercent() {
		return discountPercent;
	}

	public Boolean isProductAlive() {
		return isProductAlive;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setProductStock(Integer productStock) {
		this.productStock = productStock;
	}

	public void setMaxBuyProduct(Integer maxBuyProduct) {
		this.maxBuyProduct = maxBuyProduct;
	}

	public void setShippingCharges(Double shippingCharges) {
		this.shippingCharges = shippingCharges;
	}

	public void setDeliveryCharges(Double deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}

	public void setSpecialNote(String specialNote) {
		this.specialNote = specialNote;
	}

	public void setWarehouseAddress(Address warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}

	public void setProductPrice(Float productPrice) {
		this.productPrice = productPrice;
	}

	public void setDiscountPercent(Float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public void setProductAlive(Boolean isProductAlive) {
		this.isProductAlive = isProductAlive;
	}

}
