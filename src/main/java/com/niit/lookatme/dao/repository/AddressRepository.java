package com.niit.lookatme.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.Address;

@Repository("addressRepository")
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
	
}
