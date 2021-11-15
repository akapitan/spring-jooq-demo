/*
 * Copyright (C) 2021 - 2021 Philips Domestic Appliances Holding B.V.
 * All rights reserved.
 */

package com.akapitan.demo.springjooqddd;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.jooq.ConnectionProvider;
import org.jooq.ExecuteListenerProvider;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.boot.autoconfigure.jooq.JooqProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

@TestConfiguration
@EnableConfigurationProperties({JooqProperties.class})
public class PostgreSqlContainerConfiguration {

  @Bean
  public PostgreSQLContainer<?> postgreSqlContainer() {
    PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres:14.1");
    postgreSqlContainer.start();
    try (JdbcDatabaseDelegate delegate = new JdbcDatabaseDelegate(postgreSqlContainer, "")) {
      delegate.execute("create extension if not exists \"uuid-ossp\"", "", 0, false, false);
    }
    return postgreSqlContainer;
  }

  @Bean
  public HikariDataSource dataSource(PostgreSQLContainer<?> postgreSqlContainer) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(postgreSqlContainer.getJdbcUrl());
    dataSource.setUsername(postgreSqlContainer.getUsername());
    dataSource.setPassword(postgreSqlContainer.getPassword());
    return dataSource;
  }

  @Bean
  public DefaultDSLContext dslContext(org.jooq.Configuration configuration) {
    new Settings().setExecuteLogging(false);
    return new DefaultDSLContext(configuration);
  }

  @Bean
  @ConditionalOnMissingBean({org.jooq.Configuration.class})
  public DefaultConfiguration jooqConfiguration(
      JooqProperties properties, ConnectionProvider connectionProvider, DataSource dataSource,
      ObjectProvider<ExecuteListenerProvider> executeListenerProviders,
      ObjectProvider<DefaultConfigurationCustomizer> configurationCustomizers) {
    DefaultConfiguration configuration = new DefaultConfiguration();
    configuration.set(properties.determineSqlDialect(dataSource));
    configuration.set(connectionProvider);
    configuration.set(
        executeListenerProviders.orderedStream().toArray(
            ExecuteListenerProvider[]::new));
    configurationCustomizers.orderedStream().forEach((customizer) -> customizer.customize(configuration));
    return configuration;
  }

  @Bean
  @ConditionalOnMissingBean({ConnectionProvider.class})
  public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
    return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
  }
}

