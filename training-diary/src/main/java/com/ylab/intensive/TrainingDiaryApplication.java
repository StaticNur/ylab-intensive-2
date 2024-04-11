package com.ylab.intensive;

import com.ylab.intensive.config.MigrationManager;
import com.ylab.intensive.di.context.ApplicationContext;
import com.ylab.intensive.di.factory.BeanFactory;

import java.time.Duration;

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
        Duration duration = Duration.parse("PT4H56M45S");
        System.out.println(duration.toHours() + "ч. "+duration.toMinutesPart()+"м. "+duration.toSecondsPart()+"c. ");

        /*TrainingDiaryApplication application = new TrainingDiaryApplication();
        ApplicationContext context = application.initAppContext();
        ApplicationRunner applicationRunner = context.getBean(ApplicationRunner.class);

        MigrationManager.migrate();

        applicationRunner.run();*/
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