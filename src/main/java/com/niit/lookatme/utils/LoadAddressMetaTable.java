package com.niit.lookatme.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

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
	public void init() {
		GeoNameListAll geoNameListAll = webClientBuilder.build().get().uri(baseChild, baseGeoName).retrieve()
				.bodyToMono(GeoNameListAll.class).block();

		List<AddressMetaInner> addressMetaInnerList = new ArrayList<>();
		Map<String, Integer> countryToStates = geoNameListAll.getGeonames().stream()
				.collect(Collectors.toMap(Geoname::getName, Geoname::getGeonameId));
		Map<String, List<Mono<GeoNameListAll>>> mapMonoStateToCities = new HashMap<>();
		List<Mono<GeoNameListAll>> listMonoStateToCities = new ArrayList<>();
		countryToStates.entrySet().stream().forEach(x -> {
			Mono<GeoNameListAll> stateToCityGeoNames = webClientBuilder.build().get().uri(baseChild, x.getValue())
					.retrieve().bodyToMono(GeoNameListAll.class);
			listMonoStateToCities.add(stateToCityGeoNames);
			mapMonoStateToCities.put(x.getKey(), listMonoStateToCities);
		});

		mapMonoStateToCities.entrySet().stream().forEach(x -> {
			x.getValue().stream().forEach(y -> {
				GeoNameListAll innerChildGeoNameListAll = y.block();
				Map<String, List<Mono<GeoNameListAll>>> mapMonoCityToSubCities = new HashMap<>();
				List<Mono<GeoNameListAll>> listMonoCityToSubCities = new ArrayList<>();
				innerChildGeoNameListAll.getGeonames().stream().forEach(z -> {
					Mono<GeoNameListAll> cityToSubCityGeoNames = webClientBuilder.build().get()
							.uri(baseChild, z.getGeonameId()).retrieve().bodyToMono(GeoNameListAll.class);
					listMonoCityToSubCities.add(cityToSubCityGeoNames);
					mapMonoCityToSubCities.put(z.getName(), listMonoCityToSubCities);
				});

				mapMonoCityToSubCities.entrySet().stream().forEach(k -> {
					k.getValue().stream().map(reactor.core.publisher.Mono::block).map(GeoNameListAll::getGeonames)
							.forEach(a -> {
								a.stream().forEach(b -> {
									addressMetaInnerList.add(new AddressMetaInner(x.getKey(), k.getKey(), b.getName()));
									System.out.println(x.getKey() + " ------------ "+ k.getKey() + " ------------ "+ b.getName());
								});

							});

				});

			});
		});
		System.out.println(addressMetaInnerList.size());
	}
}
