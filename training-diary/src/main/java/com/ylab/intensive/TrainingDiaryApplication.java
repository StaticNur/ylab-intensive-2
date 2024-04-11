package com.ylab.intensive;

import com.ylab.intensive.controller.ApplicationRunner;
import com.ylab.intensive.util.MigrationManager;
import com.ylab.intensive.di.context.ApplicationContext;
import com.ylab.intensive.di.factory.BeanFactory;

/**
 * The main entry point for the application.
 */
public class TrainingDiaryApplication {
    /**
     * The main method of the application.
     * Initializes the application context and starts migration.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TrainingDiaryApplication application = new TrainingDiaryApplication();
        ApplicationContext context = application.initAppContext();
        ApplicationRunner applicationRunner = context.getBean(ApplicationRunner.class);

        MigrationManager migrationManager = context.getBean(MigrationManager.class);
        migrationManager.migrate();

        applicationRunner.run();
    }

    /**
     * Initializes the application context and the bean factory.
     *
     * @return the application context
     */
    public ApplicationContext initAppContext() {
        ApplicationContext applicationContext = new ApplicationContext();
        BeanFactory beanFactory = new BeanFactory(applicationContext);
        applicationContext.setBeanFactory(beanFactory);

        return applicationContext;
    }

}