package com.niit.lookatme.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.niit.lookatme.dao.customer.Customer;

@Entity
@Table(name="CHAIR_ALLOCATE")
public class ChairAllocate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8460984586580948005L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CHAIR_ALLOCATE_ID")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "FLOOR")
	@ColumnDefault("'GROUND'")
	private Floor floor;
	
	@Column(name = "NUM", nullable=false, unique = true)
	private String num;//F1C2
	
	@Column(name = "IS_OCCUPIED")
	@ColumnDefault("false")
	private Boolean occupied;
	
	@Column(name = "IS_AVAILABLE")
	@ColumnDefault("true")
	private Boolean available;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER", updatable = false, referencedColumnName = "CUSTOMER_ID")
	private Customer customer;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the floor
	 */
	public Floor getFloor() {
		return floor;
	}

	/**
	 * @return the num
	 */
	public String getNum() {
		return num;
	}

	/**
	 * @return the occupied
	 */
	public Boolean getOccupied() {
		return occupied;
	}

	/**
	 * @return the available
	 */
	public Boolean getAvailable() {
		return available;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * @param occupied the occupied to set
	 */
	public void setOccupied(Boolean occupied) {
		this.occupied = occupied;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(Boolean available) {
		this.available = available;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	
}
