package com.ylab.intensive.aspects;

import com.ylab.intensive.aspects.annotation.Auditable;
import com.ylab.intensive.model.dto.JwtResponse;
import com.ylab.intensive.model.dto.UserDto;
import com.ylab.intensive.repository.AuditDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

    @AfterReturning(pointcut = "callAuditableMethod()", returning = "result")
    public void audit(JoinPoint jp, Object result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        StringBuilder builder = new StringBuilder();
        String email = authentication.getName();
        if (result instanceof ResponseEntity<?> responseEntity) {
            Object body = responseEntity.getBody();
            if (body instanceof JwtResponse jwtResponse) {
                email = jwtResponse.login();
            } else if (body instanceof UserDto userDto) {
                email = userDto.getEmail();
            }
        }
        if (!email.equals("anonymousUser")) {
            MethodSignature methodSignature = (MethodSignature) jp.getSignature();
            Auditable audit = methodSignature.getMethod().getAnnotation(Auditable.class);
            String action = audit.action();

            builder.append("User: ")
                    .append(email)
                    .append(", with role: ")
                    .append(authentication.getAuthorities().stream()
                            .findAny().map(GrantedAuthority::toString).orElse("empty"))
                    .append(", execute: ").append(action);
            String uuid = jp.getArgs().length > 0 ? jp.getArgs()[0].toString() : "";
            action = builder.toString().replace("@uuid", uuid);

            User user = userService.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException("User with email = " + authentication.getName() + " does not exist!"));
            auditDao.insertUserAction(user.getId(), action);
        }

    }
}
