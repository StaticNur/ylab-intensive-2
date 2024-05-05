package com.ylab.intensive.aspects;

import com.ylab.intensive.repository.AuditDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * The AuditAspect class defines an aspect responsible for auditing method executions.
 * <p>
 * This aspect intercepts method invocations of methods annotated with the @Auditable annotation
 * and logs information about their execution based on the specific method being called.
 * It distinguishes different actions based on the method name and logs appropriate audit messages.
 * <p>
 * Audit messages are logged before the method execution after
 * the method execution (in the @AfterReturning advice).
 * <p>
 * Note that the actual logging behavior is dependent on the AuditService, which is responsible
 * for inserting audit log entries into a database.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditDao auditDao;
    private final UserService userService;


    @Pointcut(value = "@annotation(com.ylab.intensive.aspects.annotation.Auditable) && execution(* *(..))")
    public void callAuditableMethod() {
    }

    @AfterReturning("callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        System.out.println("Aspect audit");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            StringBuilder builder = new StringBuilder();
            builder.append("User: ").append(authentication.getName())
                    .append(", with role: ")
                    .append(authentication.getAuthorities().stream().findAny().map(GrantedAuthority::toString)
                            .orElse("empty"))
                    .append(", execute: ").append(joinPoint.getSignature().getDeclaringType().getName())
                    .append(".").append(joinPoint.getSignature().getName())
                    .append("(");
            Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg).append(", "));
            builder.delete(builder.length() - 2, builder.length());

            User user = userService.findByEmail(authentication.getName())
                    .orElseThrow(() -> new NotFoundException("There is no user with this login in the database."));

            auditDao.insertUserAction(user.getId(), builder.toString());
        }
    }
}
