package com.niit.lookatme.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.niit.lookatme.dao.services.Service;

@Repository("serviceRepository")
public interface ServiceRepository extends CrudRepository<Service, Long> {

	Optional<Service> findByName(String name);

	@Query("select case when count(s)> 0 then true else false end from Service s LEFT JOIN FETCH s.serviceGroup st where s.name = :name and s.hsn = :hsn and st.name = :stName")
	Boolean existsByNameHsnType(@Param("name") String name, @Param("hsn") String hsn, @Param("stName") String stName);

	@Query("select s from Service s LEFT JOIN FETCH s.serviceGroup st where s.name = :name and s.hsn = :hsn and st.name = :stName and s.isActive = TRUE")
	Optional<Service> findActiveByNameHsnType(@Param("name") String name, @Param("hsn") String hsn,
			@Param("stName") String stName);

	@Query("select s from Service s LEFT JOIN FETCH s.serviceGroup st where s.name = :name and s.hsn = :hsn and st.name = :stName and s.isActive = FALSE")
	Optional<Service> findInactiveByNameHsnType(@Param("name") String name, @Param("hsn") String hsn,
			@Param("stName") String stName);
	
	@Query("select s from Service s LEFT JOIN FETCH s.serviceGroup st where s.name = :name and s.hsn = :hsn and st.name = :stName")
	Optional<Service> findByNameHsnType(@Param("name") String name, @Param("hsn") String hsn,
			@Param("stName") String stName);
	
	@Query("select s from Service s where s.isActive = TRUE")
	List<Service> findAllActiveServices();
	
	@Query("select s from Service s LEFT JOIN FETCH s.serviceGroup st where st.name = :stName and s.isActive = TRUE")
	List<Service> findAllActiveServicesByType(@Param("stName") String stName);

}
