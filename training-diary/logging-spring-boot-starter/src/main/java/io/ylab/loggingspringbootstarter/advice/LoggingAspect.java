package io.ylab.loggingspringbootstarter.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * The LoggableAspect class defines an aspect that handles logging for methods or types
 * annotated with the Loggable annotation.
 * <p>
 * This aspect intercepts method invocations of methods annotated with @Loggable
 * and logs information about their execution, including method name and execution time.
 * <p>
 * The logging behavior is defined using the Around advice, which intercepts the method
 * execution and executes custom logging code before and after the method call.
 */
@Aspect
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("io.ylab.loggingspringbootstarter.pointcut.Pointcuts.callLoggableMethod()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder();
        builder.append("class: ").append(joinPoint.getSignature().getDeclaringType().getName());
        builder.append(", method: ").append(joinPoint.getSignature().getName());
        builder.append(", params: ");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
        log.info("before: {}", builder);
        Object retVal = joinPoint.proceed();
        log.info("after: {}", builder);
        System.out.println("Loggable: " + builder);
        return retVal;
    }
}
