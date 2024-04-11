package com.ylab.intensive;

import com.ylab.intensive.config.MigrationManager;
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

        MigrationManager.migrate();

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

/*System.out.print("\u001B[42;5;18m \u001B[1;30m Здравствуйте, добро пожаловать в консольное приложения! Следуйте указаниям из документации. \n\u001B[0m");


        System.out.print("\u001B[1;31m Пользователь с таким id не найден. Либо у этого пользователя нет действий \n\u001B[0m");
        System.out.println("\u001B[42;5;18m\u001B[1;30m Результат запроса (логи): ");
        System.out.print("\n\u001B[0m");
        System.err.println("date.toString()");

        System.out.println("\u001B[43;5;18m\u001B[1;30m Введите user_id для просмотра действий пользователя ");
        System.out.print(" Пример2: 34\n\u001B[0m");
        System.err.print("Показания уже были поданы для данного месяца\n");*/