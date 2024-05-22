package com.ylab.intensive.repository.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {"com.ylab.intensive.repository"})
public class TestRepositories {
}
