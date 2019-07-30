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
import com.niit.lookatme.dto.Postalcode;
import com.niit.lookatme.dto.StateToZip;

import reactor.core.publisher.Mono;

/**
 * 
 * @author Konika
 *
 */
@Component
public class LoadAddressMetaTable {

	@Resource
	private WebClient.Builder webClientBuilder;

	@Value("${geoname.base.child}")
	private String baseChild;

	@Value("${base.geoname}")
	private String baseGeoName;

	@Value("${geoname.postal.codes.by.city}")
	private String postalByCity;

	@Resource(name = "addressMetaRepository")
	private AddressMetaRepository addressMetaRepository;

	private class RegionZipMono {
		private Mono<GeoNameListAll> geoNameListAllMono;
		private Mono<StateToZip> stateToZipMono;

		private RegionZipMono(Mono<GeoNameListAll> geoNameListAllMono, Mono<StateToZip> stateToZipMono) {
			super();
			this.geoNameListAllMono = geoNameListAllMono;
			this.stateToZipMono = stateToZipMono;
		}

		public Mono<GeoNameListAll> getGeoNameListAllMono() {
			return geoNameListAllMono;
		}

		public Mono<StateToZip> getStateToZipMono() {
			return stateToZipMono;
		}

	}

	//@PostConstruct
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
			Map<String, Map<String, RegionZipMono>> map = new HashMap<>();
			for (Entry<String, Mono<GeoNameListAll>> stateToCitites : mapMonoStateToCities.entrySet()) {
				Map<String, RegionZipMono> stateToCitiesListMap = new HashMap<>();
				stateToCitites.getValue().block().getGeonames().stream().forEach(x -> {
					Mono<GeoNameListAll> stateToCityGeoNames = webClientBuilder.build().get()
							.uri(baseChild, x.getGeonameId()).retrieve().bodyToMono(GeoNameListAll.class);

					Mono<StateToZip> stateToZipPostCodes = webClientBuilder.build().get().uri(postalByCity, x.getName())
							.retrieve().bodyToMono(StateToZip.class);

					RegionZipMono regionZipMono = new RegionZipMono(stateToCityGeoNames, stateToZipPostCodes);

					stateToCitiesListMap.put(x.getName(), regionZipMono);
					map.put(stateToCitites.getKey(), stateToCitiesListMap);

				});
			}
			List<AddressMeta> addressMetaList = new ArrayList<>();
			for (Entry<String, Map<String, RegionZipMono>> mapper : map.entrySet())
				for (Entry<String, RegionZipMono> child : mapper.getValue().entrySet())
					addressMetaList.add(new AddressMeta("India", mapper.getKey(), child.getKey(),
							child.getValue().getGeoNameListAllMono().block().getGeonames().stream()
									.map(Geoname::getName).distinct().collect(Collectors.joining("~")),
							child.getValue().getStateToZipMono().block().getPostalcodes().stream()
									.map(Postalcode::getPostalcode).distinct().collect(Collectors.joining())));
			addressMetaRepository.saveAll(addressMetaList);
		}
	}
}
