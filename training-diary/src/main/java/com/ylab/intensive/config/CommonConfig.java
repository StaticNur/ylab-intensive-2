package com.ylab.intensive.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.aspects.AuditAspect;
import com.ylab.intensive.aspects.SecurityAspect;
import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.security.impl.JwtTokenServiceImpl;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.ContextManager;
import com.ylab.intensive.util.Converter;
import com.ylab.intensive.model.JwtProperties;
import com.ylab.intensive.util.PropertiesUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

/**
 * Configuration class for common application components.
 * <p>
 * This class defines methods for producing various beans used across the application,
 * such as converters, JWT token services, security aspects, and audit aspects.
 * It also injects dependencies into these components where necessary.
 * </p>
 *
 * @since 1.0
 */
@ApplicationScoped
public class CommonConfig {

    /**
     * Produces a {@link Converter} bean configured with an {@link ObjectMapper}.
     * <p>
     * This method creates a {@code Converter} bean initialized with an {@code ObjectMapper}
     * configured to find and register modules automatically.
     * </p>
     *
     * @return a {@code Converter} bean for converting JSON data.
     */
    @Produces
    @Named("converter")
    public Converter converter() {
        return new Converter(new ObjectMapper().findAndRegisterModules());
    }

    /**
     * Produces a {@link JwtTokenService} bean configured with JWT properties.
     * <p>
     * This method creates a {@code JwtTokenService} bean initialized with JWT properties
     * retrieved from the application's configuration.
     * </p>
     *
     * @return a {@code JwtTokenService} bean for handling JWT tokens.
     */
    @Produces
    @Named("jwtTokenService")
    public JwtTokenService jwtTokenService() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(PropertiesUtil.get("jwt.secret"));
        jwtProperties.setAccess(Long.parseLong(PropertiesUtil.get("jwt.access")));
        jwtProperties.setRefresh(Long.parseLong(PropertiesUtil.get("jwt.refresh")));
        return new JwtTokenServiceImpl(jwtProperties);
    }

    /**
     * Produces a {@link SecurityAspect} bean for enforcing security measures.
     * <p>
     * This method creates a {@code SecurityAspect} bean configured with a context manager
     * for managing application context.
     * </p>
     *
     * @param contextManager the context manager for accessing application context.
     * @return a {@code SecurityAspect} bean for enforcing security measures.
     */
    @Produces
    @Named("securityAspect")
    public SecurityAspect securityAspect(ContextManager contextManager) {
        System.out.println("создали SecurityAspect");

        SecurityAspect securityAspect = SecurityAspect.aspectOf();
        securityAspect.inject(contextManager);
        return securityAspect;
    }

    /**
     * Produces an {@link AuditAspect} bean for auditing method invocations.
     * <p>
     * This method creates an {@code AuditAspect} bean configured with a context manager,
     * an audit DAO, and a user service for auditing purposes.
     * </p>
     *
     * @param contextManager the context manager for accessing application context.
     * @param auditDao       the data access object for auditing.
     * @param userService    the user service for retrieving user information.
     * @return an {@code AuditAspect} bean for auditing method invocations.
     */
    @Produces
    @Named("auditAspect")
    public AuditAspect auditAspect(ContextManager contextManager, AuditDao auditDao, UserService userService) {
        System.out.println("создали AuditAspect");

        AuditAspect auditAspect = AuditAspect.aspectOf();
        auditAspect.inject(contextManager, auditDao, userService);
        return auditAspect;
    }
}
