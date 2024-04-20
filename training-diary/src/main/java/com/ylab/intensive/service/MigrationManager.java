package com.ylab.intensive.service;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.ui.AnsiColor;
import com.ylab.intensive.util.PropertiesUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MigrationManager Class.
 * This class is responsible for migrating data during application startup.
 * It adds some initial users and workouts to the user database.
 */
public class MigrationManager {
    /**
     * Output data.
     * Allows outputting data to the user.
     */
    @Inject
    private OutputData outputData;

    /**
     * ANSI color.
     * Allows setting the color for console output.
     */
    @Inject
    private AnsiColor ansiColor;

    /**
     * Performs database migration using Liquibase.
     * Retrieves a connection from the connection pool, initializes Liquibase with changelog file,
     * and performs the migration. Prints a success message if migration is completed successfully,
     * or an error message if any exception occurs.
     */
    public void migrate() {
        try (Connection connection = ConnectionManager.get()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            String changeLogFile = PropertiesUtil.get("liquibase.changeLogFile");
            String schemaName = PropertiesUtil.get("liquibase.schemaName");

            createSchemaForMigration(connection, schemaName);
            database.setLiquibaseSchemaName(schemaName);
            Liquibase liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            outputData.output(ansiColor.yellowText("Миграция данных успешно завершена!"));
        } catch (LiquibaseException e) {
            outputData.errOutput("SQL Exception in migration " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a schema if it does not already exist.
     *
     * @param connection the database connection
     * @param schemaName the name of the schema to create
     * @throws SQLException if a database access error occurs
     */
    private void createSchemaForMigration(Connection connection, String schemaName) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName;
        statement.executeUpdate(sql);
        statement.close();
    }
}
