package io.ylab.loggingspringbootstarter.pointcut;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Utility class containing pointcut definitions.
 * <p>
 * This class defines pointcuts for methods annotated with {@code @Loggable} and {@code @Timed}.
 */
public class Pointcuts {
    /**
     * Pointcut definition for methods annotated with {@code @Loggable}.
     */
    @Pointcut("@annotation(io.ylab.loggingspringbootstarter.annotation.Loggable)")
    public void callLoggableMethod() {
    }

    /**
     * Pointcut definition for methods annotated with {@code @Timed}.
     */
    @Pointcut("@annotation(io.ylab.loggingspringbootstarter.annotation.Timed)")
    public void callTimedMethod() {
    }
}
