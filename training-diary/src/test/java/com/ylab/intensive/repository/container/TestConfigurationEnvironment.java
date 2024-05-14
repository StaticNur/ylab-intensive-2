package com.ylab.intensive.repository.container;

import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestConfigurationEnvironment {
    protected static PostgreSQLContainer<?> postgreSQLContainer;
    protected static JdbcTemplate jdbcTemplate;
}

