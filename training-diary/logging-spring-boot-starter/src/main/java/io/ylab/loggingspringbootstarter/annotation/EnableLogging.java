package io.ylab.loggingspringbootstarter.annotation;

import io.ylab.loggingspringbootstarter.config.LoggingConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to enable logging configuration.
 * <p>
 * This annotation enables logging configuration defined in {@link LoggingConfiguration}.
 * It should be applied to the main class of the Spring Boot application.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(LoggingConfiguration.class)
public @interface EnableLogging {
}
