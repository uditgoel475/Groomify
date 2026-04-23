package com.uditgoel.groomify.utils;

import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;

import org.springframework.stereotype.Component;

import com.uditgoel.groomify.dao.GovtIdType;
import com.uditgoel.groomify.dao.repository.GovtIdTypeRepository;

@Component
public class LoadGovtIDTypeTable {

	@Resource
	private GovtIdTypeRepository govtIdTypeRepository;

	@Resource
	private LoadGovtIDTypeTableConfig loadGovtIDTypeTableConfig;

	@PostConstruct
	public void init() {

		if (govtIdTypeRepository.count() == 0) {

			govtIdTypeRepository.saveAll(loadGovtIDTypeTableConfig.getGovtIdTypes().stream()
					.map(govtIdType -> new GovtIdType(govtIdType.getIdType(), govtIdType.getRegex()))
					.collect(Collectors.toList()));
		}

	}
}
