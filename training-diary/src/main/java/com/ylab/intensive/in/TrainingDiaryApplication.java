package com.ylab.intensive.in;

import com.ylab.intensive.config.ConnectionManager;
import com.ylab.intensive.config.MigrationManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.NoArgsConstructor;

/**
 * The main entry point for the application.
 */
@WebListener
@ApplicationScoped
@NoArgsConstructor
public class TrainingDiaryApplication implements ServletContextListener {

    /**
     * Initializes the application context when the servlet context is initialized.
     * And performs database migration
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationManager migrationManager = new MigrationManager();
        migrationManager.migrate();
    }

    /**
     * Destroys the application context when the servlet context is destroyed.
     *
     * @param sce the servlet context event
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionManager.closePool();
        ServletContextListener.super.contextDestroyed(sce);
    }
}
