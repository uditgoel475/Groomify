package com.niit.lookatme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
public class LookAtMeApplication {

	public static void main(String[] args) {
		SpringApplication.run(LookAtMeApplication.class, args);
	}
	
	@Bean
	public WebClient.Builder webClientBuilder(){
		return WebClient.builder();
	}

}
