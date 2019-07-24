package com.niit.lookatme.utils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class LoadAddressMetaTable {

	@Resource
	private WebClient.Builder webClientBuilder;

	@Value("${geoname.base.child}")
	private String baseChild;

	@Value("${base.geoname}")
	private String baseGeoName;

	public class AddressMetaInner {
		private String state;
		private String city;
		private String region;

		private AddressMetaInner(String state, String region, String city) {
			this.state = state;
			this.city = city;
			this.region = region;
		}

		public String getState() {
			return state;
		}

		public String getCity() {
			return city;
		}

		public String getRegion() {
			return region;
		}
	}

	@PostConstruct
	public void init() {/*
		GeoNameListAll geoNameListAll = webClientBuilder.build().get().uri(baseChild, baseGeoName).retrieve()
				.bodyToMono(GeoNameListAll.class).block();

		List<AddressMetaInner> addressMetaInnerList = new ArrayList<>();
		Map<String, Integer> countryToStates = geoNameListAll.getGeonames().stream()
				.collect(Collectors.toMap(Geoname::getName, Geoname::getGeonameId));//state n geocode
		
		Map<String, Mono<GeoNameListAll>> mapMonoStateToCities = new HashMap<>();
		
		for(Entry<String, Integer> countryToState : countryToStates.entrySet()) {
			Mono<GeoNameListAll> stateToCityGeoNames = webClientBuilder.build().get().uri(baseChild, countryToState.getValue())
					.retrieve().bodyToMono(GeoNameListAll.class);
			mapMonoStateToCities.put(countryToState.getKey(), stateToCityGeoNames);
		}
		Map<String, Map<String, Mono<GeoNameListAll>>> map = new HashMap<>();
		for(Entry<String, Mono<GeoNameListAll>> stateToCitites : mapMonoStateToCities.entrySet()) {
			Map<String, Mono<GeoNameListAll>> smallMap = new HashMap<>();
			stateToCitites.getValue().block().getGeonames().stream().forEach(x->{
				Mono<GeoNameListAll> stateToCityGeoNames = webClientBuilder.build().get().uri(baseChild, x.getGeonameId())
						.retrieve().bodyToMono(GeoNameListAll.class);
				smallMap.put(x.getName(), stateToCityGeoNames);
				map.put(stateToCitites.getKey(), smallMap);
			});
		}
		
		for(Entry<String, Map<String, Mono<GeoNameListAll>>> mapper : map.entrySet()) {
			for(Entry<String, Mono<GeoNameListAll>> child : mapper.getValue().entrySet())
				for(Geoname name : child.getValue().block().getGeonames()) {
					addressMetaInnerList.add(new AddressMetaInner(mapper.getKey(), child.getKey(), name.getName()));
				}
		}
	*/}
}
