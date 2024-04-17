package com.ylab.intensive.di.configurator;

import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Configurator for mapping interface classes to their implementations using Java reflections.
 */
public class JavaBeanConfigurator implements BeanConfigurator {
    private final Reflections scanner;
    private final Map<Class, Class> interfaceToImplementation;

    /**
     * Constructs a new JavaBeanConfigurator with the specified package to scan and interface to implementation mappings.
     *
     * @param packageToScan             the package to scan for implementations
     * @param interfaceToImplementation the map containing interface to implementation mappings
     */
    public JavaBeanConfigurator(String packageToScan, Map<Class, Class> interfaceToImplementation) {
        this.scanner = new Reflections(packageToScan);
        this.interfaceToImplementation = new ConcurrentHashMap<>(interfaceToImplementation);
    }

    /**
     * Retrieves the implementation class for the specified interface class.
     *
     * @param interfaceClass the interface class for which to retrieve the implementation
     * @param <T>            the type of the interface
     * @return the implementation class for the specified interface
     * @throws RuntimeException if there are no implementations or more than one implementation for the interface
     */
    @Override
    public <T> Class<? extends T> getImplementationClass(Class<T> interfaceClass) {
        return interfaceToImplementation.computeIfAbsent(interfaceClass, clazz -> {
            Set<Class<? extends T>> implementationClass = scanner.getSubTypesOf(interfaceClass);
            if (implementationClass.size() != 1) {
                throw new RuntimeException("Interface has 0 or more than 1 implementations");
            }

            return implementationClass.stream().findFirst().get();
        });
    }
}