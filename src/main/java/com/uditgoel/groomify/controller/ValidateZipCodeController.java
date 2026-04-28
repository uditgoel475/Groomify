package com.uditgoel.groomify.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uditgoel.groomify.service.GeoNameService;

/**
 *
 * @author ugoel1
 *
 */

@RestController
@RequestMapping("/api/validate")
public class ValidateZipCodeController {

	private final GeoNameService geoNameService;

	private Map<String, String> countries;

	public ValidateZipCodeController(@Qualifier("geoNameService") GeoNameService geoNameService) {
		this.geoNameService = geoNameService;
	}

	@PostConstruct
	public void init() {
		countries = new HashMap<>();
		for (String iso : Locale.getISOCountries()) {
			Locale l = new Locale(StringUtils.EMPTY, iso);
			countries.put(l.getDisplayCountry(), iso);
		}
	}

	@GetMapping("zipcode/{city}/{country}/{postalCode}")
	public ResponseEntity<Boolean> validateZipCode(@PathVariable(required = true) String city,
			@PathVariable(required = true) String country, @PathVariable(required = true) int postalCode) {
		return ResponseEntity
				.ok(geoNameService.validateZipCodeFromCityAndCountry(city, countries.get(country), postalCode));
	}
}
