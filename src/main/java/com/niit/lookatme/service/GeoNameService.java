package com.niit.lookatme.service;

public interface GeoNameService {

	boolean validateZipCodeFromCityAndCountry(String city, String countryCode, int postalCode);
}
