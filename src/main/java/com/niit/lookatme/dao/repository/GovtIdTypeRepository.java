package com.niit.lookatme.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.GovtIdType;

@Repository("govtIdTypeRepository")
public interface GovtIdTypeRepository extends CrudRepository<GovtIdType, Long> {

	Optional<GovtIdType> findByTypeName(String typeName);
}
