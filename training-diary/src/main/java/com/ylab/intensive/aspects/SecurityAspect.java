package com.ylab.intensive.aspects;

import com.ylab.intensive.exception.AuthorizeException;
import com.ylab.intensive.aspects.annotation.AllowedRoles;
import com.ylab.intensive.security.Authentication;
import com.ylab.intensive.util.ContextManager;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
@Log4j2
public class SecurityAspect {

    private static final SecurityAspect INSTANCE = new SecurityAspect();
    private ContextManager contextManager;

    public static SecurityAspect aspectOf() {
        return INSTANCE;
    }

    public void inject(ContextManager contextManager) {
        this.contextManager = contextManager;
    }

    @Pointcut(value = "@annotation(allowedRoles) && execution(* *(..)) ")
    public void callAllowedRolesMethod(AllowedRoles allowedRoles) {
    }

    @Before(value = "callAllowedRolesMethod(allowedRoles)", argNames = "allowedRoles")
    public void secure(AllowedRoles allowedRoles) {
        System.out.println("Aspect auth");
        HttpSession session = contextManager.getBean(HttpSession.class);
        Authentication user = (Authentication) session.getAttribute("authentication");
        if (user == null) {
            throw new AuthorizeException("Authentication required");
        }
        if (Arrays.stream(allowedRoles.value())
                .noneMatch(role -> user.getRole().equals(role))) {
            throw new AuthorizeException("Access denied.");
        }
    }
}