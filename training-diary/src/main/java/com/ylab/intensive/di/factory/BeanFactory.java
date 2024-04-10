package com.ylab.intensive.di.factory;

import com.ylab.intensive.di.annatation.Inject;
import com.ylab.intensive.di.config.Configuration;
import com.ylab.intensive.di.config.JavaConfiguration;
import com.ylab.intensive.di.configurator.BeanConfigurator;
import com.ylab.intensive.di.configurator.JavaBeanConfigurator;
import com.ylab.intensive.di.context.ApplicationContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Factory class for creating beans and handling dependency injection.
 */
public class BeanFactory {
    private final Configuration configuration;
    private final BeanConfigurator beanConfigurator;
    private final ApplicationContext applicationContext;

    /**
     * Constructs a new BeanFactory with the specified ApplicationContext.
     *
     * @param applicationContext the application context to be used
     */
    public BeanFactory(ApplicationContext applicationContext) {
        this.configuration = new JavaConfiguration();
        this.beanConfigurator = new JavaBeanConfigurator(configuration.getPackageToScan(), configuration.getInterfaceToImplementation());
        this.applicationContext = applicationContext;
    }

    /**
     * Retrieves a bean of the specified class from the application context.
     *
     * @param clazz the class of the bean to retrieve
     * @param <T>   the type of the bean to retrieve
     * @return the retrieved bean
     * @throws RuntimeException if an error occurs during bean creation or injection
     */
    @SneakyThrows
    public <T> T getBean(Class<T> clazz) {
        Class<? extends T> implementationClass = clazz;
        if (implementationClass.isInterface()) {
            implementationClass = beanConfigurator.getImplementationClass(implementationClass);
        }
        T bean = implementationClass.getDeclaredConstructor().newInstance();

        for (Field field : Arrays.stream(implementationClass.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Inject.class)).toList()) {
            field.setAccessible(true);
            field.set(bean, applicationContext.getBean(field.getType()));
        }
        return bean;
    }
}