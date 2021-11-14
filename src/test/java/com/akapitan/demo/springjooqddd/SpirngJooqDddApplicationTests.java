package com.akapitan.demo.springjooqddd;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(PostgreSqlContainerConfiguration.class)
class SpirngJooqDddApplicationTests {

  @Test
  void contextLoads() {
  }

}
