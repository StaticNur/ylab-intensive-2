package io.ylab.loggingspringbootstarter.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Aspect for measuring the execution time of annotated methods.
 * <p>
 * This aspect intercepts method calls annotated with {@code Timed} and measures
 * the time taken for their execution. It logs the execution time for each method.
 * </p>
 *
 * @since 1.0
 */
@Aspect
public class TimingAspect {
    private static final Logger log = LoggerFactory.getLogger(TimingAspect.class);

    @Around("io.ylab.loggingspringbootstarter.pointcut.Pointcuts.callTimedMethod()")
    public Object timed(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder();
        builder.append(joinPoint.getSignature().getDeclaringType().getName());
        builder.append(".").append(joinPoint.getSignature().getName());
        builder.append("(");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
        long before = System.currentTimeMillis();
        Object retVal = joinPoint.proceed();
        long after = System.currentTimeMillis();
        builder.append("execution time: ").append(after - before).append("ms");
        log.info(builder.toString());
        System.out.println("Timed: " + builder);
        return retVal;
    }
}
