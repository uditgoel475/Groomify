/**
 * 
 */
package com.niit.lookatme.inventory.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.niit.lookatme.customer.dao.Customer;
import com.niit.lookatme.dao.AuditInfo;

/**
 * @author ugoel1
 *
 */

@Entity
@Table(name = "STOCK_HISTORY")
public class StockHistory extends AuditInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3985414697147471756L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STOCK_HISTORY_ID", nullable = false, updatable = false)
    private Long id;
	
	@Enumerated(value = EnumType.STRING)
	@Column(name = "STOCK_ACTION", nullable = false)
	private StockAction stockAction;
	
	@Column(name = "QUANTITY_CHANGE")
	private Integer quantityChange;
	
	@Column(name = "PRICE_SOLD_PER_UNIT")
	private Float priceSoldPerUnit;
	
	@Column(name = "STOCK_QUANTITY")
	private Integer stockQuantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT", referencedColumnName = "PRODUCT_ID")
	private Product product;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER", referencedColumnName = "CUSTOMER_ID")
	private Customer customer;

	@Column(name = "NOTES")
	private String notes;
	
	/**
	 * @return the stockAction
	 */
	public StockAction getStockAction() {
		return stockAction;
	}

	/**
	 * @param stockAction the stockAction to set
	 */
	public void setStockAction(StockAction stockAction) {
		this.stockAction = stockAction;
	}

	/**
	 * @return the quantityChange
	 */
	public Integer getQuantityChange() {
		return quantityChange;
	}

	/**
	 * @param quantityChange the quantityChange to set
	 */
	public void setQuantityChange(Integer quantityChange) {
		this.quantityChange = quantityChange;
	}

	/**
	 * @return the priceSoldPerUnit
	 */
	public Float getPriceSoldPerUnit() {
		return priceSoldPerUnit;
	}

	/**
	 * @param priceSoldPerUnit the priceSoldPerUnit to set
	 */
	public void setPriceSoldPerUnit(Float priceSoldPerUnit) {
		this.priceSoldPerUnit = priceSoldPerUnit;
	}

	/**
	 * @return the stockQuantity
	 */
	public Integer getStockQuantity() {
		return stockQuantity;
	}

	/**
	 * @param stockQuantity the stockQuantity to set
	 */
	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
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
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	} 
}
