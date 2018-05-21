package com.mh.model.stage;

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
 * Clase de configuración JDBC. Provee conexión a la base de datos stage
 * 
 * @author arosorio@gmail.com
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "stageEntityManagerFactory", transactionManagerRef = "stageTransactionManager", basePackages = {
		"com.mh.model.stage" })
public class StageConfig {

	@Bean(name = "stageDataSource")
	@ConfigurationProperties(prefix = "spring.stage.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "stageEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("stageDataSource") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.mh.model.stage").persistenceUnit("stage").build();
	}

	@Bean(name = "stageTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("stageEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	@Bean(name = "stageJdbcTemplate")
	@Autowired
	public NamedParameterJdbcTemplate jdbcTemplate(@Qualifier("stageDataSource") DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}

}
