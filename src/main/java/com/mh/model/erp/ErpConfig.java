package com.mh.model.erp;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Clase de configuración JDBC. Provee conexión a la base de datos origen
 * 
 * @author arosorio@gmail.com
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "erpEntityManagerFactory", transactionManagerRef = "erpTransactionManager", basePackages = {
		"com.mh.model.erp" })
public class ErpConfig {

	@Bean(name = "erpDataSource")
	@ConfigurationProperties(prefix = "spring.erp.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "erpEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("erpDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.mh.model.erp").persistenceUnit("erp").build();
	}

	@Bean(name = "erpTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("erpEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean(name = "erpJdbcTemplate")
	@Autowired
	public NamedParameterJdbcTemplate jdbcTemplate(@Qualifier("erpDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
}
