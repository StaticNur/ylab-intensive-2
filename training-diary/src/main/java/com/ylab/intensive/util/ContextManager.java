package com.ylab.intensive.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * This class manages CDI bean instances and provides a method to retrieve beans by their class.
 * It is typically used in CDI-managed components to obtain instances of other beans dynamically.
 */
@ApplicationScoped
@NoArgsConstructor
public class ContextManager {

    private BeanManager beanManager;

    /**
     * Constructs a new ContextManager with the specified BeanManager.
     *
     * @param beanManager The BeanManager instance provided by the CDI container.
     */
    @Inject
    public ContextManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    /**
     * Retrieves a bean instance of the specified class.
     *
     * @param <T>    The type of the bean to retrieve.
     * @param tClass The class of the bean to retrieve.
     * @return An instance of the specified bean class.
     * @throws IllegalStateException if no CDI bean is found for the specified class.
     */
    public <T> T getBean(Class<T> tClass) {
        Set<Bean<?>> beans = beanManager.getBeans(tClass);
        if (beans.isEmpty()) {
            throw new IllegalStateException("CDI bean not found for type: " + tClass);
        }
        Bean<?> bean = beanManager.resolve(beans);
        return tClass.cast(beanManager.getReference(bean, tClass, beanManager.createCreationalContext(bean)));
    }
}

