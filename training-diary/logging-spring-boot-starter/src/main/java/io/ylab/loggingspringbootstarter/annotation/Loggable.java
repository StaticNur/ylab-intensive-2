package io.ylab.loggingspringbootstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Loggable annotation is used to mark methods or types that are subject to logging.
 * Methods or types annotated with this annotation can have their executions logged.
 * <p>
 * This annotation can be applied to both methods and types (classes and interfaces).
 * <p>
 * Note that logging is performed using an aspect-oriented programming (AOP) approach,
 * so the actual logging behavior is defined in a separate aspect (LoggableAspect).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Loggable {
}
