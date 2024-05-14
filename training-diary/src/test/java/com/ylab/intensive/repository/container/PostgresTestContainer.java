package com.ylab.intensive.repository.container;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresTestContainer extends PostgreSQLContainer<PostgresTestContainer> {

    private static final String IMAGE_VERSION = "postgres:15-alpine";

    @Container
    private static PostgresTestContainer container;

    private PostgresTestContainer() {
        super(IMAGE_VERSION);
    }

    @Override
    public void start() {
        super.start();
        migration();
    }

    @Override
    public void stop() {
        super.stop();
    }

    public static JdbcTemplate getJdbcTemplate() {
        DataSource dataSource = createDataSource();
        return new JdbcTemplate(dataSource);
    }

    public static PostgresTestContainer getInstance() {
        container = new PostgresTestContainer()
                .withDatabaseName("fake_db")
                .withUsername("test_user")
                .withPassword("test_psw")
                .withCreateContainerCmdModifier(cmd -> cmd.withPortBindings(new PortBinding(Ports.
                        Binding.bindPort(5434), new ExposedPort(5432))));
        container.start();
        return container;
    }

    private void migration() {
        try {
            Connection connection = DriverManager.getConnection(container.getJdbcUrl(),
                    container.getUsername(), container.getPassword());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            createSchemaForMigration(connection, "migration");

            database.setLiquibaseSchemaName("migration");
            Liquibase liquibase = new Liquibase("db.changelog/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static DataSource createDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUser(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }

    private void createSchemaForMigration(Connection connection, String schemaName) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        statement.executeUpdate(sql);
        statement.close();
    }
}
