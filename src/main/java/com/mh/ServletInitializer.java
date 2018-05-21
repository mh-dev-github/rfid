package com.mh;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * Especialización la clase abstracta SpringBootServletInitializer. Requerido para desplegar la aplicación en un archivo war
 * 
 * @author arosorio@gmail.com
 * 
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/servlet/support/SpringBootServletInitializer.html">SpringBootServletInitializer</a>
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
}
