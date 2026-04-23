package com.uditgoel.groomify.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI groomifyOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("Groomify REST API")
				.description("Salon booking backend — customers, employees, services, orders")
				.version("1.0")
				.contact(new Contact()
						.name("Udit Goel")
						.url("https://github.com/uditgoel475/Groomify")
						.email("goeludit1990@gmail.com"))
				.license(new License()
						.name("MIT License")
						.url("https://opensource.org/licenses/MIT")));
	}
}
