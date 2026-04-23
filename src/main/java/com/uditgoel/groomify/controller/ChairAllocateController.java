package com.uditgoel.groomify.controller;

import java.util.List;

import jakarta.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uditgoel.groomify.dao.Floor;
import com.uditgoel.groomify.dto.ChairAllocateDTO;
import com.uditgoel.groomify.facade.ChairAllocateFacade;

@RestController
@RequestMapping("api/chair")
public class ChairAllocateController {
	
	@Resource
	private ChairAllocateFacade chairAllocateFacade;
	
	@GetMapping("available/floor/{floor}")
	public ResponseEntity<List<String>> fetchFreeAvailableChairsGivenFloor(@PathVariable("floor") Floor floor) {
		return ResponseEntity.ok(chairAllocateFacade.fetchFreeAvailableChairsGivenFloor(floor));
	}
	
	@GetMapping("allocated/customer/{username}")
	public ResponseEntity<ChairAllocateDTO> fetchChairAllocatedByCustomer(@PathVariable("username") String username) {
		return ResponseEntity.ok(chairAllocateFacade.fetchChairAllocatedByCustomer(username));
	}
	
	@GetMapping("available/floor/{floor}/{num}")
	public ResponseEntity<Boolean> findChairByNumOnFloor(@PathVariable("floor") Floor floor, @PathVariable("num") String num) {
		return ResponseEntity.ok(chairAllocateFacade.findChairByNum(floor, num));
	}
	
	@PostMapping("allocate/customer/{username}/num/{num}")
	public ResponseEntity<String> allocateChairByNumber(@PathVariable("username") String username, @PathVariable("num") String num) {
		return ResponseEntity.ok(chairAllocateFacade.allocateChair(username, num));
	}
	
	@PostMapping("allocate/customer/{username}/floor/{floor}")
	public ResponseEntity<String> allocateChairByFloor(@PathVariable("username") String username, @PathVariable("floor") Floor floor) {
		return ResponseEntity.ok(chairAllocateFacade.allocateChair(floor, username));
	}
	
	@PostMapping("unallocate/num/{num}")
	public ResponseEntity<Boolean> unAllocateChairByNum(@PathVariable("num") String num) {
		return ResponseEntity.ok(chairAllocateFacade.unAllocateChair(num));
	}
	
	@PostMapping("unallocate/customer/{username}")
	public ResponseEntity<Boolean> unAllocateChairByCustomer(@PathVariable("username") String username) {
		return ResponseEntity.ok(chairAllocateFacade.unAllocateChairByCustomer(username));
	}
	
	@PostMapping("remove/{num}")
	public ResponseEntity<Boolean> removeChair(@PathVariable("num") String num) {
		return ResponseEntity.ok(chairAllocateFacade.removeChair(num));
	}
	
	@PostMapping("expire/{num}")
	public ResponseEntity<Boolean> expireChair(@PathVariable("num") String num) {
		return ResponseEntity.ok(chairAllocateFacade.expireChair(num));
	}
	
	@PostMapping("add/{floor}/{count}")
	public ResponseEntity<Integer> expireChair(@PathVariable("count") int count, @PathVariable("floor") Floor floor) {
		return ResponseEntity.ok(chairAllocateFacade.addChairs(floor, count));
	}
}
