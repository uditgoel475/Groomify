package com.uditgoel.groomify.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.AddressMeta;

/**
 * 
 * @author Konika
 *
 */
@Repository("addressMetaRepository")
public interface AddressMetaRepository extends JpaRepository<AddressMeta, Long> {

}
