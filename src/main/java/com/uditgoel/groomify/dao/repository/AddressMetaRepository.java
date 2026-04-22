package com.uditgoel.groomify.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.AddressMeta;

/**
 * 
 * @author Konika
 *
 */
@Repository("addressMetaRepository")
public interface AddressMetaRepository extends PagingAndSortingRepository<AddressMeta, Long> {

}
