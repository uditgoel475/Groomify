package com.uditgoel.groomify.utils;

import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.uditgoel.groomify.dao.GovtIdType;
import com.uditgoel.groomify.dao.repository.GovtIdTypeRepository;

@Component
public class LoadGovtIDTypeTable {

	private final GovtIdTypeRepository govtIdTypeRepository;

	private final LoadGovtIDTypeTableConfig loadGovtIDTypeTableConfig;

	public LoadGovtIDTypeTable(GovtIdTypeRepository govtIdTypeRepository,
			LoadGovtIDTypeTableConfig loadGovtIDTypeTableConfig) {
		this.govtIdTypeRepository = govtIdTypeRepository;
		this.loadGovtIDTypeTableConfig = loadGovtIDTypeTableConfig;
	}

	@PostConstruct
	@Transactional
	public void init() {

		if (govtIdTypeRepository.count() == 0) {

			govtIdTypeRepository.saveAll(loadGovtIDTypeTableConfig.getGovtIdTypes().stream()
					.map(govtIdType -> new GovtIdType(govtIdType.getIdType(), govtIdType.getRegex()))
					.collect(Collectors.toList()));
		}

	}
}
