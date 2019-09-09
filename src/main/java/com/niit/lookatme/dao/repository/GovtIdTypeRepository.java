package com.niit.lookatme.dao.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.GovtIdType;

@Repository("govtIdTypeRepository")
public interface GovtIdTypeRepository extends PagingAndSortingRepository<GovtIdType, Long> {

	Optional<GovtIdType> findByTypeNameOrderByTypeNameAsc(String typeName);
}
