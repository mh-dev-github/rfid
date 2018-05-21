package com.mh.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.util.UrlPathHelper;


/**
 * Clase de configuración Spring MVC. Requerido para permitir el uso del caracter ";" en los parametros de las URLs con el fin de pasar matrices como parametros. 
 * 
 * @author arosorio@gmail.com
 *
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		UrlPathHelper urlPathHelper = new UrlPathHelper();
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}
}