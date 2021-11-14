/*
 * Copyright (C) 2021 - 2021 Philips Domestic Appliances Holding B.V.
 * All rights reserved.
 */

package com.akapitan.demo.springjooqddd;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

@TestConfiguration
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

}
