package com.uditgoel.groomify.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.GovtIdType;

@Repository("govtIdTypeRepository")
public interface GovtIdTypeRepository extends JpaRepository<GovtIdType, Long> {

	Optional<GovtIdType> findByTypeNameOrderByTypeNameAsc(String typeName);
}
