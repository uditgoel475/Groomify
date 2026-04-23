package com.uditgoel.groomify.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.Address;

@Repository("addressRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {
	
}
