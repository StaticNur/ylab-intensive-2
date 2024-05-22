package io.ylab.auditspringbootstarter.service.impl;

import io.ylab.auditspringbootstarter.annotation.Auditable;
import io.ylab.auditspringbootstarter.service.MessageBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class MessageBuilderImpl implements MessageBuilder {

    @Override
    public String generate(JoinPoint jp, String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
        String action = audit.action();

        String builder = "User: " + email + ", with role: " +
                         authentication.getAuthorities().stream()
                                 .findAny()
                                 .map(GrantedAuthority::toString)
                                 .orElse("empty") +
                         ", execute: " + action;
        String uuid = jp.getArgs().length > 0 ? jp.getArgs()[0].toString() : "";
        return builder.replace("@uuid", uuid);
    }
}
