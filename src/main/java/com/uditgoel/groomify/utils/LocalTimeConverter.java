package com.uditgoel.groomify.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class LocalTimeConverter implements Converter<String, LocalTime> {
	
	@Override
	public LocalTime convert(String source) {
		if (StringUtils.isEmpty(source)) {
			return null;
		}
		return LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss"));
	}
}