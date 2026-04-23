package com.uditgoel.groomify.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uditgoel.groomify.dao.ChairAllocate;
import com.uditgoel.groomify.dao.Floor;

@Repository("chairAllocateRepository")
public interface ChairAllocateRepository extends JpaRepository<ChairAllocate, Long> {

	@Query("select c.num from ChairAllocate c where c.floor = :floor and c.available = true and c.occupied = false order by c.num")
	List<String> findFreeAvailableChairsGivenFloor(@Param("floor") Floor floor);
	
	@Query("select ca from ChairAllocate ca LEFT JOIN FETCH ca.customer c where c.username = :username and ca.occupied = true")
	Optional<ChairAllocate> findChairAllocateByCustomer(@Param("username") String username);
	
	@Query("select CASE WHEN COUNT(c) > 0 THEN true ELSE false END from ChairAllocate c where c.floor = :floor and c.num = :num and c.available = true and c.occupied = false")
	boolean findAvailableByNum(@Param("floor") Floor floor, @Param("num") String num);
	
	Optional<ChairAllocate> findFirstByNum(String num);
	
	@Query("select count(1) from ChairAllocate c where c.floor = :floor")
	Integer chairCountOnFloor(@Param("floor") Floor floor);
	
	Optional<ChairAllocate> findFirstByFloorAndAvailableAndOccupied(Floor floor, Boolean available, Boolean occupied);
	
}
