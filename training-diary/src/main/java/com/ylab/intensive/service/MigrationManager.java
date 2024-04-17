package com.ylab.intensive.service;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.in.OutputData;
import com.ylab.intensive.ui.AnsiColor;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

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

            Liquibase liquibase = new Liquibase("db.changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            outputData.output(ansiColor.yellowText("Миграция данных успешно завершена!"));
        } catch (LiquibaseException e) {
            outputData.errOutput("SQL Exception in migration " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
