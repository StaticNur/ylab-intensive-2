package com.ylab.intensive.aspects;

import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.exception.NotFoundException;
import com.ylab.intensive.model.entity.User;
import com.ylab.intensive.security.Authentication;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.ContextManager;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
public class AuditAspect {

    private static final AuditAspect INSTANCE = new AuditAspect();
    private ContextManager contextManager;
    private AuditDao auditDao;
    private UserService userService;

    public static AuditAspect aspectOf() {
        return INSTANCE;
    }

    public void inject(ContextManager contextManager, AuditDao auditDao, UserService userService) {
        this.contextManager = contextManager;
        this.auditDao = auditDao;
        this.userService = userService;
    }

    @Pointcut(value = "@annotation(com.ylab.intensive.aspects.annotation.AllowedRoles) && execution(* *(..))")
    public void callAuditableMethod() {
    }

    @AfterReturning("callAuditableMethod()")
    public void audit(JoinPoint joinPoint) {
        System.out.println("Aspect audit");
        StringBuilder builder = new StringBuilder();
        HttpServletRequest httpServletRequest = contextManager.getBean(HttpServletRequest.class);
        Authentication authentication = (Authentication) httpServletRequest.getAttribute("authentication");

        builder.append("User: ").append(authentication.getLogin())
                .append(", with role: ").append(authentication.getRole())
                .append(", path: ").append(httpServletRequest.getContextPath())
                .append(", execute: ").append(joinPoint.getSignature().getDeclaringType().getName())
                .append(".").append(joinPoint.getSignature().getName())
                .append("(");
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> builder.append(arg).append(", "));
        builder.delete(builder.length() - 2, builder.length());

        User user = userService.findByEmail(authentication.getLogin())
                .orElseThrow(() -> new NotFoundException("There is no user with this login in the database."));

        auditDao.insertUserAction(user.getId(), builder.toString());
    }
}
