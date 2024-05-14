package com.ylab.intensive.util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * A factory for creating property sources from YAML files.
 */
public class YamlPropertiesUtilFactory implements PropertySourceFactory {

    /**
     * Creates a property source from the given YAML resource.
     *
     * @param name            the name of the property source
     * @param encodedResource the YAML resource to load properties from
     * @return the property source created from the YAML resource
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) throws IOException {
        YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
        yamlFactory.setResources(new ClassPathResource("application.yml"));

        Properties properties = yamlFactory.getObject();

        if (properties == null) {
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
        }

        return new PropertiesPropertySource("yamlPropertySource", properties);
    }
}
