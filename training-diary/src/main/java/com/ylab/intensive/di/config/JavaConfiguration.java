package com.ylab.intensive.di.config;

import java.util.Collections;
import java.util.Map;

/**
 * Default implementation of the Configuration interface for Java-based configuration.
 */
public class JavaConfiguration implements Configuration {

    /**
     * Retrieves the package to scan for components.
     *
     * @return the package name to scan
     */
    @Override
    public String getPackageToScan() {
        return "com.ylab.intensive";
    }

    /**
     * Gets a mapping of interfaces to their preferred implementations if there are more than one.
     *
     * @return the map containing interface to implementation mappings
     */
    @Override
    public Map<Class, Class> getInterfaceToImplementation() {
        return Collections.EMPTY_MAP;
    }
}