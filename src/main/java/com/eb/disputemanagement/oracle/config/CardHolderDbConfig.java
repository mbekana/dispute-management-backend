package com.eb.disputemanagement.oracle.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "cardHolderEntityManagerFactory",
		transactionManagerRef = "cardHolderTransactionManager",
		basePackages = {"com.eb.disputemanagement.oracle.cardholder"}
)
public class CardHolderDbConfig {

	@Bean(name="cardHoldersDatasource")
	@ConfigurationProperties(prefix = "db2.datasource")
	public DataSource dataSource(){
		return DataSourceBuilder.create().build();
	}

	@Bean(name="cardHolderEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(
			EntityManagerFactoryBuilder builder,
			@Qualifier("cardHoldersDatasource") DataSource dataSource
	)
	{
		return builder
				.dataSource(dataSource)
				.packages("com.eb.disputemanagement.oracle.cardholder")
				.persistenceUnit("cardholder")
				.build();
	}

	@Bean(name="cardHolderTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("cardHolderEntityManagerFactory")
					EntityManagerFactory cardHolderEntityManagerFactory
	)
	{
		return new JpaTransactionManager(cardHolderEntityManagerFactory);
	}
}
