package com.mh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.mh.api.sync.servicios.purge.PedidosPurgeService;
import com.mh.api.sync.servicios.purge.EntradasProductoPurgeService;
import com.mh.api.sync.servicios.purge.OrdenesProduccionPurgeService;
import com.mh.api.sync.servicios.purge.SalidasTiendaPurgeService;

@SpringBootApplication
@EnableScheduling
public class Application implements CommandLineRunner {
	@Autowired
	PedidosPurgeService pedidosPurgeService;
	@Autowired
	SalidasTiendaPurgeService salidasTiendasPurgeService;
	@Autowired
	EntradasProductoPurgeService entradasProductosTerminadosPurgeService;
	@Autowired
	OrdenesProduccionPurgeService ordenesProduccionPurgeService;

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

	@Override
	public void run(String... strings) throws Exception {
//		pedidosPurgeService.purge();
//		salidasTiendasPurgeService.purge();
//		entradasProductosTerminadosPurgeService.purge();
//		ordenesProduccionPurgeService.purge();
//		productosPurgeService.purge();
//		locacionesPurgeService.purge();
	}
}
