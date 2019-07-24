package com.niit.lookatme.inventory.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import com.niit.lookatme.dao.AuditInfo;

/**
 * This class maps to table PRODUCT which holds the salon's inventory items for
 * E-Commerce
 * 
 * @author Konika
 *
 */
@Entity
@Table(name = "PRODUCTS")
public class Product extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7649020378685905850L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PRODUCT_ID", nullable = false, updatable = false)
	private Long id;

	@Column(name = "SKU")
	private String productSKU;// Stock Keeping Unit

	@NotNull(message = "Product name is required.")
	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "PRICE", nullable = false)
	private float price;

	@Column(name = "WEIGHT", nullable = false)
	private float weight;

	@Column(name = "PRODUCT_CART_DESC", nullable = false)
	private String productCartDesc;

	@Column(name = "PRODUCT_SHORT_DESC")
	private String productShortDesc;

	@Column(name = "PRODUCT_LONG_DESC")
	private String productLongDesc;

	@Column(name = "PRODUCT_THUMB_URL")
	private String productThumbUrl;

	@Column(name = "PRODUCT_IMAGE_URL_1")
	private String productImageURL1;

	@Column(name = "PRODUCT_IMAGE_URL_2")
	private String productImageURL2;

	@Column(name = "PRODUCT_IMAGE_URL_3")
	private String productImageURL3;

	@Column(name = "PRODUCT_IMAGE_URL_4")
	private String productImageURL4;

	@Column(name = "PRODUCT_IMAGE_URL_5")
	private String productImageURL5;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_CATEGORY", referencedColumnName = "PRODUCT_CATEGORY_ID")
	private ProductCategory productCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_PRODUCT_STOCK", referencedColumnName = "VENDOR_PRODUCT_STOCK_ID")
	private VendorProductStock vendorProductStock;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "PRODUCT_EXPIRATION_DATE")
	private Date productExpirationDate;

	@Column(name = "IS_HOT", nullable = false)
	@ColumnDefault("false")
	private Boolean isHot;

	public Long getId() {
		return id;
	}

	public String getProductSKU() {
		return productSKU;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}

	public float getWeight() {
		return weight;
	}

	public String getProductCartDesc() {
		return productCartDesc;
	}

	public String getProductShortDesc() {
		return productShortDesc;
	}

	public String getProductLongDesc() {
		return productLongDesc;
	}

	public String getProductThumbUrl() {
		return productThumbUrl;
	}

	public String getProductImageURL1() {
		return productImageURL1;
	}

	public String getProductImageURL2() {
		return productImageURL2;
	}

	public String getProductImageURL3() {
		return productImageURL3;
	}

	public String getProductImageURL4() {
		return productImageURL4;
	}

	public String getProductImageURL5() {
		return productImageURL5;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public VendorProductStock getVendorProductStock() {
		return vendorProductStock;
	}

	public Date getProductExpirationDate() {
		return productExpirationDate;
	}

	public Boolean isHot() {
		return isHot;
	}

	public void setProductSKU(String productSKU) {
		this.productSKU = productSKU;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void setProductCartDesc(String productCartDesc) {
		this.productCartDesc = productCartDesc;
	}

	public void setProductShortDesc(String productShortDesc) {
		this.productShortDesc = productShortDesc;
	}

	public void setProductLongDesc(String productLongDesc) {
		this.productLongDesc = productLongDesc;
	}

	public void setProductThumbUrl(String productThumbUrl) {
		this.productThumbUrl = productThumbUrl;
	}

	public void setProductImageURL1(String productImageURL1) {
		this.productImageURL1 = productImageURL1;
	}

	public void setProductImageURL2(String productImageURL2) {
		this.productImageURL2 = productImageURL2;
	}

	public void setProductImageURL3(String productImageURL3) {
		this.productImageURL3 = productImageURL3;
	}

	public void setProductImageURL4(String productImageURL4) {
		this.productImageURL4 = productImageURL4;
	}

	public void setProductImageURL5(String productImageURL5) {
		this.productImageURL5 = productImageURL5;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public void setVendorProductStock(VendorProductStock vendorProductStock) {
		this.vendorProductStock = vendorProductStock;
	}

	public void setProductExpirationDate(Date productExpirationDate) {
		this.productExpirationDate = productExpirationDate;
	}

	public void setHot(Boolean isHot) {
		this.isHot = isHot;
	}

}
