package com.uditgoel.groomify.dto;

import com.uditgoel.groomify.dao.Floor;
import com.uditgoel.groomify.dto.customer.CustomerDTO;

public class ChairAllocateDTO {

	private Floor floor;
	private String num;
	private CustomerDTO customerDTO;

	public ChairAllocateDTO() {super();}
	public ChairAllocateDTO(Floor floor, String num,
			CustomerDTO customerDTO) {
		super();
		this.floor = floor;
		this.num = num;
		this.customerDTO = customerDTO;
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
	 * @return the customerDTO
	 */
	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}

	/**
	 * @param floor
	 *            the floor to set
	 */
	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(String num) {
		this.num = num;
	}

	/**
	 * @param customerDTO
	 *            the customerDTO to set
	 */
	public void setCustomerDTO(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}
}
