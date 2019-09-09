package com.niit.lookatme.dao.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.AddressMeta;

/**
 * 
 * @author Konika
 *
 */
@Repository("addressMetaRepository")
public interface AddressMetaRepository extends PagingAndSortingRepository<AddressMeta, Long> {

}
