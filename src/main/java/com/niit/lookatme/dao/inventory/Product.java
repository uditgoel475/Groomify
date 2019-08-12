package com.niit.lookatme.dao.inventory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

	@Column(name = "HSN/SAC")
	private String hsn;// Stock Keeping Unit

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "COST_PRICE", nullable = false)
	private float costPrice;
	
	@Column(name = "FULL_PRICE", nullable = false)
	private float fullPrice;
	
	@Column(name = "SPECIAL_PRICE")
	private float specialPrice;

	@Column(name = "WEIGHT", nullable = false)
	private String weight;

	@Column(name = "PRODUCT_SHORT_DESC")
	private String productShortDesc;

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

	@Column(name = "IN_STOCK_QUANTITY")
	private Integer inStockQuantity;

	@Column(name = "QUANTITY_ALERT")
	private Integer quantityAlert;
	
	@Column(name = "BARCODE", unique= true)
	private String barcode;

	/**
	 * @return the barcode
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * @param barcode the barcode to set
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	/**
	 * @return the hsn
	 */
	public String getHsn() {
		return hsn;
	}

	/**
	 * @param hsn the hsn to set
	 */
	public void setHsn(String hsn) {
		this.hsn = hsn;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the costPrice
	 */
	public float getCostPrice() {
		return costPrice;
	}

	/**
	 * @param costPrice the costPrice to set
	 */
	public void setCostPrice(float costPrice) {
		this.costPrice = costPrice;
	}

	/**
	 * @return the fullPrice
	 */
	public float getFullPrice() {
		return fullPrice;
	}

	/**
	 * @param fullPrice the fullPrice to set
	 */
	public void setFullPrice(float fullPrice) {
		this.fullPrice = fullPrice;
	}

	/**
	 * @return the specialPrice
	 */
	public float getSpecialPrice() {
		return specialPrice;
	}

	/**
	 * @param specialPrice the specialPrice to set
	 */
	public void setSpecialPrice(float specialPrice) {
		this.specialPrice = specialPrice;
	}

	/**
	 * @return the weight
	 */
	public String getWeight() {
		return weight;
	}

	/**
	 * @param weight the weight to set
	 */
	public void setWeight(String weight) {
		this.weight = weight;
	}

	/**
	 * @return the productShortDesc
	 */
	public String getProductShortDesc() {
		return productShortDesc;
	}

	/**
	 * @param productShortDesc the productShortDesc to set
	 */
	public void setProductShortDesc(String productShortDesc) {
		this.productShortDesc = productShortDesc;
	}

	/**
	 * @return the productThumbUrl
	 */
	public String getProductThumbUrl() {
		return productThumbUrl;
	}

	/**
	 * @param productThumbUrl the productThumbUrl to set
	 */
	public void setProductThumbUrl(String productThumbUrl) {
		this.productThumbUrl = productThumbUrl;
	}

	/**
	 * @return the productImageURL1
	 */
	public String getProductImageURL1() {
		return productImageURL1;
	}

	/**
	 * @param productImageURL1 the productImageURL1 to set
	 */
	public void setProductImageURL1(String productImageURL1) {
		this.productImageURL1 = productImageURL1;
	}

	/**
	 * @return the productImageURL2
	 */
	public String getProductImageURL2() {
		return productImageURL2;
	}

	/**
	 * @param productImageURL2 the productImageURL2 to set
	 */
	public void setProductImageURL2(String productImageURL2) {
		this.productImageURL2 = productImageURL2;
	}

	/**
	 * @return the productImageURL3
	 */
	public String getProductImageURL3() {
		return productImageURL3;
	}

	/**
	 * @param productImageURL3 the productImageURL3 to set
	 */
	public void setProductImageURL3(String productImageURL3) {
		this.productImageURL3 = productImageURL3;
	}

	/**
	 * @return the productImageURL4
	 */
	public String getProductImageURL4() {
		return productImageURL4;
	}

	/**
	 * @param productImageURL4 the productImageURL4 to set
	 */
	public void setProductImageURL4(String productImageURL4) {
		this.productImageURL4 = productImageURL4;
	}

	/**
	 * @return the productImageURL5
	 */
	public String getProductImageURL5() {
		return productImageURL5;
	}

	/**
	 * @param productImageURL5 the productImageURL5 to set
	 */
	public void setProductImageURL5(String productImageURL5) {
		this.productImageURL5 = productImageURL5;
	}

	/**
	 * @return the productCategory
	 */
	public ProductCategory getProductCategory() {
		return productCategory;
	}

	/**
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * @return the inStockQuantity
	 */
	public Integer getInStockQuantity() {
		return inStockQuantity;
	}

	/**
	 * @param inStockQuantity the inStockQuantity to set
	 */
	public void setInStockQuantity(Integer inStockQuantity) {
		this.inStockQuantity = inStockQuantity;
	}

	/**
	 * @return the quantityAlert
	 */
	public Integer getQuantityAlert() {
		return quantityAlert;
	}

	/**
	 * @param quantityAlert the quantityAlert to set
	 */
	public void setQuantityAlert(Integer quantityAlert) {
		this.quantityAlert = quantityAlert;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
}
