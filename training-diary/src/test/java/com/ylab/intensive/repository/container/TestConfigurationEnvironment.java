package com.ylab.intensive.repository.container;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestConfigurationEnvironment {
    protected static PostgreSQLContainer<?> postgreSQLContainer;
}

