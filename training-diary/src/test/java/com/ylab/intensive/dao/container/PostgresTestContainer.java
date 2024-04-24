package com.ylab.intensive.dao.container;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.util.PropertiesUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.sql.Connection;
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
        ConnectionManager.closePool();
        super.stop();
    }

    public static PostgresTestContainer getInstance() {
        container = new PostgresTestContainer()
                .withDatabaseName("fake_db")
                .withUsername("test_user")
                .withPassword("test_psw")
                .withCreateContainerCmdModifier(cmd -> cmd.withPortBindings(new PortBinding(Ports.
                        Binding.bindPort(5435), new ExposedPort(5432))));
        container.start();
        return container;
    }

    private void migration() {
        try {
            Connection connection = ConnectionManager.get();
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            String changeLogFile = PropertiesUtil.get("liquibase.changeLogFile");
            String schemaName = PropertiesUtil.get("liquibase.schemaName");

            createSchemaForMigration(connection, schemaName);

            database.setLiquibaseSchemaName(schemaName);
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
        } catch (LiquibaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createSchemaForMigration(Connection connection, String schemaName) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        statement.executeUpdate(sql);
        statement.close();
    }
}
