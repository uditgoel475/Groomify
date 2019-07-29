package com.niit.lookatme.controller;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.niit.lookatme.service.GeoNameService;

/**
 * 
 * @author ugoel1
 *
 */

@RestController
@RequestMapping("api/validate")
public class ValidateZipCodeController {

	@Resource(name = "geoNameService")
	private GeoNameService geoNameService;

	private Map<String, String> countries;

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
