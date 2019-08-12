package com.niit.lookatme.facade.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.niit.lookatme.dao.ChairAllocate;
import com.niit.lookatme.dao.Floor;
import com.niit.lookatme.dao.customer.Customer;
import com.niit.lookatme.dao.repository.ChairAllocateRepository;
import com.niit.lookatme.dao.repository.CustomerRepository;
import com.niit.lookatme.dto.ChairAllocateDTO;
import com.niit.lookatme.dto.customer.CustomerDTO;
import com.niit.lookatme.exception.AlreadyExistsException;
import com.niit.lookatme.exception.ResourceNotFoundException;
import com.niit.lookatme.facade.ChairAllocateFacade;
import com.niit.lookatme.facade.helper.CustomerFacadeHelper;

@Service("chairAllocateFacade")
public class ChairAllocateFacadeImpl implements ChairAllocateFacade {

	@Resource
	private ChairAllocateRepository chairAllocateRepository;

	@Resource
	private CustomerFacadeHelper customerFacadeHelper;

	@Resource
	private CustomerRepository customerRepository;

	@Override
	public List<String> fetchFreeAvailableChairsGivenFloor(Floor floor) {
		return chairAllocateRepository.findFreeAvailableChairsGivenFloor(floor);
	}

	@Override
	public ChairAllocateDTO fetchChairAllocatedByCustomer(String username) {
		ChairAllocate chairAllocated = findChairAllocateByCustomer(username);
		CustomerDTO customerDTO = customerFacadeHelper.createCustomerDTOFromCustomer(chairAllocated.getCustomer());
		return new ChairAllocateDTO(chairAllocated.getFloor(), chairAllocated.getNum(), customerDTO);
	}

	private ChairAllocate findChairAllocateByCustomer(String username) {
		return chairAllocateRepository.findChairAllocateByCustomer(username)
				.orElseThrow(() -> new ResourceNotFoundException("Chair", "customer", username));
	}

	@Override
	public Boolean findChairByNum(Floor floor, String num) {
		return chairAllocateRepository.findAvailableByNum(floor, num);
	}

	private ChairAllocate findFirstByNum(String num) {
		return chairAllocateRepository.findFirstByNum(num)
				.orElseThrow(() -> new ResourceNotFoundException("Chair", "ID", num));
	}

	@Override
	public String allocateChair(String num, String username) {
		ChairAllocate chairAllocate = findFirstByNum(num);
		if (chairAllocate.getOccupied())
			throw new AlreadyExistsException("Chair Already Allocated");
		chairAllocate.setCustomer(findByUsername(username));
		chairAllocate.setOccupied(true);
		chairAllocate = chairAllocateRepository.save(chairAllocate);
		return chairAllocate.getNum();
	}

	private Customer findByUsername(String username) {
		return customerRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "username", username));
	}

	@Override
	public String allocateChair(Floor floor, String username) {
		ChairAllocate chairAllocate = chairAllocateRepository
				.findFirstByFloorAndAvailableAndOccupied(floor, true, false)
				.orElseThrow(() -> new ResourceNotFoundException("Chair", "Floor", floor));
		chairAllocate.setCustomer(findByUsername(username));
		chairAllocate.setOccupied(true);
		chairAllocate = chairAllocateRepository.save(chairAllocate);
		return chairAllocate.getNum();
	}

	@Override
	public Boolean unAllocateChair(String num) {
		ChairAllocate chairAllocate = findFirstByNum(num);
		if (chairAllocate.getOccupied()) {
			chairAllocate.setOccupied(false);
			chairAllocate = chairAllocateRepository.save(chairAllocate);
		}
		return chairAllocate.getOccupied();
	}

	@Override
	public Boolean unAllocateChairByCustomer(String username) {
		ChairAllocate chairAllocate = findChairAllocateByCustomer(username);
		if (chairAllocate.getOccupied()) {
			chairAllocate.setOccupied(false);
			chairAllocate = chairAllocateRepository.save(chairAllocate);
		}
		return chairAllocate.getOccupied();
	}

	@Override
	public Boolean removeChair(String num) {
		ChairAllocate chairAllocate = findFirstByNum(num);
		chairAllocateRepository.delete(chairAllocate);
		return true;
	}

	@Override
	public Boolean expireChair(String num) {
		ChairAllocate chairAllocate = findFirstByNum(num);
		if (chairAllocate.getAvailable()) {
			chairAllocate.setAvailable(false);
			chairAllocate = chairAllocateRepository.save(chairAllocate);
		}
		return !chairAllocate.getAvailable();
	}

	@Override
	public Integer addChairs(Floor floor, int count) {
		List<ChairAllocate> chairAllocateList = new ArrayList<>();
		int currentChairCount = chairAllocateRepository.chairCountOnFloor(floor);
		for (int i = 0; i < count; i++) {
			ChairAllocate chairAllocate = new ChairAllocate();
			chairAllocate.setFloor(floor);
			chairAllocate.setNum(assignChairNum(floor, i + currentChairCount));
			chairAllocate.setOccupied(false);
			chairAllocate.setAvailable(true);
			chairAllocateList.add(chairAllocate);
		}
		chairAllocateRepository.saveAll(chairAllocateList);
		return chairAllocateRepository.chairCountOnFloor(floor) - currentChairCount;
	}

	private String assignChairNum(Floor floor, int chairNum) {
		return new StringBuilder(floor.toString()).append("C").append(chairNum + 1).toString();
	}

}
