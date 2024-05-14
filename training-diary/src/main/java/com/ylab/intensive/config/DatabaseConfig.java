package com.ylab.intensive.config;

import com.ylab.intensive.util.YamlPropertiesUtilFactory;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Configuration class for database-related settings.
 * <p>
 * This class configures the data source, transaction management, Liquibase integration,
 * and JDBC template for database operations.
 */
@Configuration
@EnableTransactionManagement
@PropertySource(value = "classpath:application.yml", factory = YamlPropertiesUtilFactory.class)
public class DatabaseConfig {

    @Value("${db.uri}")
    private String uri;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.driver-class-name}")
    private String driver;

    @Value("${liquibase.change-log}")
    private String changeLogFile;

    /**
     * Configures the data source for database connectivity.
     *
     * @return a configured DataSource instance
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setUrl(uri);
        return dataSource;
    }

    /**
     * Configures Liquibase integration for database migrations.
     *
     * @return a SpringLiquibase instance for managing database changes
     */
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(changeLogFile);
        liquibase.setDataSource(dataSource());
        return liquibase;
    }

    /**
     * Configures a JDBC template for database operations.
     *
     * @return a JdbcTemplate instance for executing SQL queries
     */
    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    /**
     * Configures the platform transaction manager for annotation-driven transaction management.
     *
     * @return a PlatformTransactionManager instance for managing transactions
     */
    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
