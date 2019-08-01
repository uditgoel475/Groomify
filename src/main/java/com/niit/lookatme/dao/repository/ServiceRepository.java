package com.niit.lookatme.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.services.dao.Service;

@Repository("serviceRepository")
public interface ServiceRepository extends CrudRepository<Service, Long> {

	Service findByName(String name);
}
