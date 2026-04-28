package com.uditgoel.groomify.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	private static final long MAX_AGE_SECS = 3600;
	private static final String[] ALLOWED_METHODS = { "HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE" };

	@Value("${groomify.cors.allowed-origins:}")
	private String allowedOriginsCsv;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String[] origins = parseOrigins(allowedOriginsCsv);
		if (origins.length == 0) {
			return;
		}
		registry.addMapping("/**")
				.allowedOrigins(origins)
				.allowedMethods(ALLOWED_METHODS)
				.allowCredentials(true)
				.maxAge(MAX_AGE_SECS);
	}

	private static String[] parseOrigins(String csv) {
		if (csv == null || csv.isBlank()) {
			return new String[0];
		}
		return csv.split("\\s*,\\s*");
	}
}
