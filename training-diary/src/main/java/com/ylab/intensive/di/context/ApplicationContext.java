package com.ylab.intensive.di.context;

import com.ylab.intensive.di.factory.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents the application context for managing beans.
 */
public class ApplicationContext {
    private BeanFactory beanFactory;
    private final Map<Class, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * Sets the bean factory for this application context.
     *
     * @param beanFactory the bean factory to be set
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Retrieves a bean of the specified class from the application context.
     *
     * @param clazz the class of the bean to retrieve
     * @param <T>   the type of the bean to retrieve
     * @return the retrieved bean
     */
    public <T> T getBean(Class<T> clazz) {
        if (beanMap.containsKey(clazz)) {
            return (T) beanMap.get(clazz);
        }
        T bean = beanFactory.getBean(clazz);
        beanMap.put(clazz, bean);
        return bean;
    }

    /**
     * Registers a bean with the specified class and instance in the application context.
     *
     * @param clazz the class of the bean to register
     * @param bean  the instance of the bean to register
     * @param <T>   the type of the bean to register
     */
    public <T> void registerBean(Class<T> clazz, T bean) {
        beanMap.put(clazz, bean);
    }
}