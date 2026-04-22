package com.uditgoel.groomify.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.Address;

@Repository("addressRepository")
public interface AddressRepository extends PagingAndSortingRepository<Address, Long> {
	
}
