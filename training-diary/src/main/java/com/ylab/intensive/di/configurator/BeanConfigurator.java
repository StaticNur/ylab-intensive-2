package com.ylab.intensive.di.configurator;

/**
 * Interface for configuring beans in dependency injection (DI) container.
 */
public interface BeanConfigurator {
    /**
     * Retrieves the implementation class for the specified interface class.
     *
     * @param interfaceClass the interface class for which to retrieve the implementation
     * @param <T>            the type of the interface
     * @return the implementation class for the specified interface
     */
    <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass);
}