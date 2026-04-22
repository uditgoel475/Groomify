package com.uditgoel.groomify.service;

public interface GeoNameService {

	boolean validateZipCodeFromCityAndCountry(String city, String countryCode, int postalCode);
}
