package io.ylab.loggingspringbootstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the execution time of a method should be measured.
 * <p>
 * Methods annotated with {@code Timed} are monitored to measure the time taken for their execution.
 * This can be useful for performance analysis and optimization.
 * </p>
 *
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Timed {
}
