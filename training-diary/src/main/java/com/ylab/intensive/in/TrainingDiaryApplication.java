package com.ylab.intensive.in;

import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.config.MigrationManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.NoArgsConstructor;

/**
 * Application listener for initializing and destroying the servlet context.
 * <p>
 * This listener is responsible for initializing the application context by performing database migrations
 * when the servlet context is initialized. It also handles cleanup tasks, such as closing database connections,
 * when the servlet context is destroyed.
 * </p>
 *
 * @since 1.0
 */
@WebListener
@ApplicationScoped
@NoArgsConstructor
public class TrainingDiaryApplication implements ServletContextListener {

    /**
     * Performs database migrations during application initialization.
     *
     * @param sce the servlet context event.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationManager migrationManager = new MigrationManager();
        migrationManager.migrate();
    }

    /**
     * Closes database connections during application shutdown.
     *
     * @param sce the servlet context event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.closePool();
        ServletContextListener.super.contextDestroyed(sce);
    }
}
