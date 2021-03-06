package com.mh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * Punto de entrada de la aplicación
 * 
 * @author arosorio@gmail.com
 *
 */
@SpringBootApplication
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@ConfigurationProperties(prefix = "custom.rest.connection")
	public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() {
		return new HttpComponentsClientHttpRequestFactory();
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder,
			HttpComponentsClientHttpRequestFactory requestFactory) {
		return builder.requestFactory(requestFactory).build();
	}
}
