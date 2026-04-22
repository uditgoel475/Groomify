package com.uditgoel.groomify.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.services.ServiceType;

@Repository("serviceTypeRepository")
public interface ServiceTypeRepository extends PagingAndSortingRepository<ServiceType, Long> {

	Optional<ServiceType> findByName(String name);
	
	Boolean existsByName(String name);
}
