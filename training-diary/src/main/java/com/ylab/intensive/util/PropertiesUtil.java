package com.ylab.intensive.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class for loading properties from a configuration file.
 */
public final class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    /**
     * Loads properties from the configuration file.
     * The configuration file should be named "application.properties" and located in the "db" directory.
     * Any IOException during loading will result in a RuntimeException.
     */
    private static void loadProperties() {
        try (InputStream properties = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the value of the property associated with the specified key.
     *
     * @param key the key of the property to retrieve
     * @return the value of the property, or null if the property does not exist
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
