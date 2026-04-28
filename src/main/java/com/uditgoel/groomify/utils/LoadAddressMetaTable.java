package com.uditgoel.groomify.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.uditgoel.groomify.dao.AddressMeta;
import com.uditgoel.groomify.dao.repository.AddressMetaRepository;
import com.uditgoel.groomify.dto.GeoNameListAll;
import com.uditgoel.groomify.dto.Geoname;
import com.uditgoel.groomify.dto.Postalcode;
import com.uditgoel.groomify.dto.StateToZip;

import reactor.core.publisher.Mono;

/**
 * One-time seed of {@code AddressMeta} from geonames.org. Disabled by default —
 * set
 * {@code groomify.bootstrap.address-meta=true} to enable. Performs blocking
 * HTTP fan-out
 * which can take several minutes; intended for first-run bootstrap only.
 */
@Component
@ConditionalOnProperty(name = "groomify.bootstrap.address-meta", havingValue = "true")
public class LoadAddressMetaTable {

	private static final Logger logger = LoggerFactory.getLogger(LoadAddressMetaTable.class);

	private final WebClient.Builder webClientBuilder;

	@Value("${geoname.base.child}")
	private String baseChild;

	@Value("${base.geoname}")
	private String baseGeoName;

	@Value("${geoname.postal.codes.by.city}")
	private String postalByCity;

	private final AddressMetaRepository addressMetaRepository;

	public LoadAddressMetaTable(WebClient.Builder webClientBuilder,
			@Qualifier("addressMetaRepository") AddressMetaRepository addressMetaRepository) {
		this.webClientBuilder = webClientBuilder;
		this.addressMetaRepository = addressMetaRepository;
	}

	private static final class RegionZipMono {
		private final Mono<GeoNameListAll> geoNameListAllMono;
		private final Mono<StateToZip> stateToZipMono;

		private RegionZipMono(Mono<GeoNameListAll> geoNameListAllMono, Mono<StateToZip> stateToZipMono) {
			this.geoNameListAllMono = geoNameListAllMono;
			this.stateToZipMono = stateToZipMono;
		}
	}

	@PostConstruct
	@Transactional
	public void init() {
		if (addressMetaRepository.count() > 0) {
			logger.info("AddressMeta already populated; skipping geonames bootstrap");
			return;
		}
		logger.info("Bootstrapping AddressMeta from geonames — this can take several minutes");

		WebClient webClient = webClientBuilder.build();

		Map<String, Integer> countryToStates = fetchStateGeonames(webClient);
		if (countryToStates.isEmpty()) {
			logger.warn("geonames returned no states; aborting bootstrap");
			return;
		}

		Map<String, Map<String, RegionZipMono>> stateToCityToMonos = fetchCityMonosByState(webClient, countryToStates);
		List<AddressMeta> addressMetaList = assembleAddressMeta(stateToCityToMonos);

		addressMetaRepository.saveAll(addressMetaList);
		logger.info("AddressMeta bootstrap complete: {} rows", addressMetaList.size());
	}

	private Map<String, Integer> fetchStateGeonames(WebClient webClient) {
		GeoNameListAll geoNameListAll = webClient.get().uri(baseChild, baseGeoName).retrieve()
				.bodyToMono(GeoNameListAll.class).block();
		if (geoNameListAll == null || geoNameListAll.getGeonames() == null) {
			return Map.of();
		}
		return geoNameListAll.getGeonames().stream()
				.collect(Collectors.toMap(Geoname::getName, Geoname::getGeonameId));
	}

	private Map<String, Map<String, RegionZipMono>> fetchCityMonosByState(WebClient webClient,
			Map<String, Integer> countryToStates) {
		Map<String, Map<String, RegionZipMono>> stateToCityToMonos = new HashMap<>();
		for (Entry<String, Integer> stateEntry : countryToStates.entrySet()) {
			GeoNameListAll citiesForState = webClient.get().uri(baseChild, stateEntry.getValue()).retrieve()
					.bodyToMono(GeoNameListAll.class).block();
			if (citiesForState == null || citiesForState.getGeonames() == null) {
				continue;
			}
			Map<String, RegionZipMono> cityToMonos = new HashMap<>();
			for (Geoname city : citiesForState.getGeonames()) {
				Mono<GeoNameListAll> areasMono = webClient.get().uri(baseChild, city.getGeonameId()).retrieve()
						.bodyToMono(GeoNameListAll.class);
				Mono<StateToZip> postcodesMono = webClient.get().uri(postalByCity, city.getName()).retrieve()
						.bodyToMono(StateToZip.class);
				cityToMonos.put(city.getName(), new RegionZipMono(areasMono, postcodesMono));
			}
			stateToCityToMonos.put(stateEntry.getKey(), cityToMonos);
		}
		return stateToCityToMonos;
	}

	private List<AddressMeta> assembleAddressMeta(Map<String, Map<String, RegionZipMono>> stateToCityToMonos) {
		List<AddressMeta> addressMetaList = new ArrayList<>();
		for (Entry<String, Map<String, RegionZipMono>> stateEntry : stateToCityToMonos.entrySet()) {
			for (Entry<String, RegionZipMono> cityEntry : stateEntry.getValue().entrySet()) {
				addressMetaList.add(buildAddressMeta(stateEntry.getKey(), cityEntry.getKey(), cityEntry.getValue()));
			}
		}
		return addressMetaList;
	}

	private AddressMeta buildAddressMeta(String state, String city, RegionZipMono regionZipMono) {
		GeoNameListAll areas = regionZipMono.geoNameListAllMono.block();
		StateToZip postcodes = regionZipMono.stateToZipMono.block();
		String areasJoined = areas == null || areas.getGeonames() == null ? ""
				: areas.getGeonames().stream().map(Geoname::getName).distinct().collect(Collectors.joining("~"));
		String postcodesJoined = postcodes == null || postcodes.getPostalcodes() == null ? ""
				: postcodes.getPostalcodes().stream().map(Postalcode::getPostalcode).distinct()
						.collect(Collectors.joining());
		return new AddressMeta("India", state, city, areasJoined, postcodesJoined);
	}
}
