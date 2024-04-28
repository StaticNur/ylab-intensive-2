package com.ylab.intensive.aspects;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
@Log4j2
public class LoggingAspect {

    @Pointcut("@within(com.ylab.intensive.aspects.annotation.Loggable) && execution(* * (..))")
    public void callLoggableMethod() {
    }

    @Around("callLoggableMethod()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        StringBuilder builder = new StringBuilder();
        builder.append("class: ").append(joinPoint.getSignature().getDeclaringType().getName());
        builder.append(", method: ").append(joinPoint.getSignature().getName());
        builder.append(", params: ");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg.toString()).append(", "));
        log.info("before: {}", builder);
        Object retVal = joinPoint.proceed();
        log.info("after: {}", builder);
        System.out.println(retVal.toString());
        return retVal;
    }
}
