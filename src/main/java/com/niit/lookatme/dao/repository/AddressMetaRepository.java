package com.niit.lookatme.dao.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.AddressMeta;

@Repository("addressMetaRepository")
public interface AddressMetaRepository extends CrudRepository<AddressMeta, Long> {

}
