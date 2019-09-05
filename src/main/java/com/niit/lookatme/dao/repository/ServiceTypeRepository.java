package com.niit.lookatme.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.services.ServiceType;

@Repository("serviceTypeRepository")
public interface ServiceTypeRepository extends CrudRepository<ServiceType, Long> {

	Optional<ServiceType> findByName(String name);
	
	Boolean existsByName(String name);
}
