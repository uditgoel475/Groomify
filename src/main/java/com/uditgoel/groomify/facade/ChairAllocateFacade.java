package com.uditgoel.groomify.facade;

import java.util.List;

import com.uditgoel.groomify.dao.Floor;
import com.uditgoel.groomify.dto.ChairAllocateDTO;

public interface ChairAllocateFacade {

	List<String> fetchFreeAvailableChairsGivenFloor(Floor floor);

	ChairAllocateDTO fetchChairAllocatedByCustomer(String username);

	Boolean findChairByNum(Floor floor, String num);

	String allocateChair(String num, String username);
	
	String allocateChair(Floor floor, String username);

	Boolean unAllocateChair(String num);

	Boolean unAllocateChairByCustomer(String username);
	
	Boolean removeChair(String num);

	Boolean expireChair(String num);

	Integer addChairs(Floor floor, int count);
}