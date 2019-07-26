/**
 * 
 */
package com.niit.lookatme.service.impl;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.niit.lookatme.dto.Postalcode;
import com.niit.lookatme.dto.StateToZip;
import com.niit.lookatme.service.GeoNameService;

/**
 * @author ugoel1
 *
 */
public class GeoNameServiceImpl implements GeoNameService {

	
	@Resource
	private WebClient.Builder webClientBuilder;

	@Value("${geoname.postal.code.validate}")
	private String postalCodeApiEndpoint;
	
	@Override
	public boolean validateZipCodeFromCityAndCountry(String city, String countryCode, int postalCode) {
		
		StateToZip stateToZip = webClientBuilder.build().get().uri(postalCodeApiEndpoint, city,countryCode,postalCode).retrieve()
				.bodyToMono(StateToZip.class).block();
		if(Optional.ofNullable(stateToZip).map(StateToZip::getPostalcodes).isPresent()) {
			return stateToZip.getPostalcodes().stream().map(Postalcode::getPostalcode).distinct().anyMatch(y->y.equals(String.valueOf(postalCode)));
		}
		return false;
	}

}
