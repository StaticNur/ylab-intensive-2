package io.ylab.auditspringbootstarter.advice;

import io.ylab.auditspringbootstarter.service.AuditRepository;
import io.ylab.auditspringbootstarter.service.MessageBuilder;
import io.ylab.auditspringbootstarter.service.UserFinder;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Method;

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
@Log4j2
public class AuditAspect {
    /**
     * Message builder for auditing.
     */
    private final MessageBuilder messageBuilder;

    /**
     * Audit repository.
     */
    private final AuditRepository auditRepository;

    /**
     * User finder.
     */
    private final UserFinder userFinder;

    public AuditAspect(MessageBuilder messageBuilder, AuditRepository auditRepository, UserFinder userFinder) {
        this.messageBuilder = messageBuilder;
        this.auditRepository = auditRepository;
        this.userFinder = userFinder;
    }

    /**
     * Performs audit of user actions.
     *
     * @param jp     Join point object.
     * @param result Method execution result.
     */
    @AfterReturning(pointcut = "io.ylab.auditspringbootstarter.pointcut.Pointcuts.callAuditableMethod()", returning = "result")
    public void audit(JoinPoint jp, Object result) {
        String email = getCurrentUserEmail();

        if (result instanceof ResponseEntity<?>) {
            email = extractEmailFromResponseEntity((ResponseEntity<?>) result, email);
        }

        if (!email.equals("anonymousUser")) {
            String action = messageBuilder.generate(jp, email);
            int userId = userFinder.findIdByEmail(email);
            auditRepository.insertUserAction(userId, action);
            log.info(action);
            System.out.println(action);
        }
    }

    /**
     * Retrieves current user's email from security context.
     *
     * @return Current user's email.
     */
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    /**
     * Extracts user email from ResponseEntity if available.
     *
     * @param responseEntity ResponseEntity containing user information.
     * @param defaultEmail   Default user email.
     * @return User email.
     */
    private String extractEmailFromResponseEntity(ResponseEntity<?> responseEntity, String defaultEmail) {
        Object body = responseEntity.getBody();
        if (body != null) {
            String className = body.getClass().getName();
            try {
                Method method = body.getClass().getMethod(
                        className.equals("com.ylab.intensive.model.dto.UserDto") ? "getEmail" : "login"
                );
                return (String) method.invoke(body);
            } catch (Exception e) {
                log.info("This isn't a registration/login.");
            }
        }
        return defaultEmail;
    }
}
