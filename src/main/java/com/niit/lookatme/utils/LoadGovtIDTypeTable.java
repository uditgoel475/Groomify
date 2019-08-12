package com.niit.lookatme.utils;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.niit.lookatme.dao.GovtIdType;
import com.niit.lookatme.dao.repository.GovtIdTypeRepository;

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
