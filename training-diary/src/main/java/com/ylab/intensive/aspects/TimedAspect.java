package com.ylab.intensive.aspects;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

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
@Log4j2
public class TimedAspect {

    @Pointcut("@within(com.ylab.intensive.aspects.annotation.Timed) && execution(* *(..))")
    public void callTimedMethod() {
    }

    @Around("callTimedMethod()")
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
        System.out.println(builder);
        return retVal;
    }
}
