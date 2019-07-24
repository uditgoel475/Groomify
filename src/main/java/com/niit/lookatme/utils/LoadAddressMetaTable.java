package com.niit.lookatme.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.niit.lookatme.dao.AddressMeta;
import com.niit.lookatme.dao.repository.AddressMetaRepository;
import com.niit.lookatme.dto.GeoNameListAll;
import com.niit.lookatme.dto.Geoname;

import reactor.core.publisher.Mono;

@Component
public class LoadAddressMetaTable {

	@Resource
	private WebClient.Builder webClientBuilder;

	@Value("${geoname.base.child}")
	private String baseChild;

	@Value("${base.geoname}")
	private String baseGeoName;

	@Resource(name = "addressMetaRepository")
	private AddressMetaRepository addressMetaRepository;

	@PostConstruct
	public void init() {
		if (addressMetaRepository.count() == 0) {
			GeoNameListAll geoNameListAll = webClientBuilder.build().get().uri(baseChild, baseGeoName).retrieve()
					.bodyToMono(GeoNameListAll.class).block();

			Map<String, Integer> countryToStates = geoNameListAll.getGeonames().stream()
					.collect(Collectors.toMap(Geoname::getName, Geoname::getGeonameId));// state n geocode

			Map<String, Mono<GeoNameListAll>> mapMonoStateToCities = new HashMap<>();

			for (Entry<String, Integer> countryToState : countryToStates.entrySet()) {
				Mono<GeoNameListAll> stateToCityGeoNames = webClientBuilder.build().get()
						.uri(baseChild, countryToState.getValue()).retrieve().bodyToMono(GeoNameListAll.class);
				mapMonoStateToCities.put(countryToState.getKey(), stateToCityGeoNames);
			}
			Map<String, Map<String, Mono<GeoNameListAll>>> map = new HashMap<>();
			for (Entry<String, Mono<GeoNameListAll>> stateToCitites : mapMonoStateToCities.entrySet()) {
				Map<String, Mono<GeoNameListAll>> smallMap = new HashMap<>();
				stateToCitites.getValue().block().getGeonames().stream().forEach(x -> {
					Mono<GeoNameListAll> stateToCityGeoNames = webClientBuilder.build().get()
							.uri(baseChild, x.getGeonameId()).retrieve().bodyToMono(GeoNameListAll.class);
					smallMap.put(x.getName(), stateToCityGeoNames);
					map.put(stateToCitites.getKey(), smallMap);
				});
			}
			List<AddressMeta> addressMetaList = new ArrayList<>();
			for (Entry<String, Map<String, Mono<GeoNameListAll>>> mapper : map.entrySet()) 
				for (Entry<String, Mono<GeoNameListAll>> child : mapper.getValue().entrySet())
					for (Geoname name : child.getValue().block().getGeonames()) 
						addressMetaList.add(new AddressMeta("India", mapper.getKey(), child.getKey(), name.getName()));
			addressMetaRepository.saveAll(addressMetaList);
		}

	}
}
