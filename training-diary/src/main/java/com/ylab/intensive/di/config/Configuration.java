package com.ylab.intensive.di.config;

import java.util.Map;

/**
 * Interface for providing configuration options for dependency injection (DI) container.
 */
public interface Configuration {
    /**
     * Retrieves the package to scan for components.
     *
     * @return the package name to scan
     */
    String getPackageToScan();

    /**
     * Retrieves the mapping of interfaces to their preferred implementations.
     *
     * @return the map containing interface to implementation mappings
     */
    Map<Class, Class> getInterfaceToImplementation();
}