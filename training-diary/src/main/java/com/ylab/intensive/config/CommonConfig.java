package com.ylab.intensive.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylab.intensive.aspects.AuditAspect;
import com.ylab.intensive.aspects.SecurityAspect;
import com.ylab.intensive.dao.AuditDao;
import com.ylab.intensive.security.JwtTokenService;
import com.ylab.intensive.security.JwtTokenServiceImpl;
import com.ylab.intensive.service.UserService;
import com.ylab.intensive.util.ContextManager;
import com.ylab.intensive.util.Converter;
import com.ylab.intensive.util.JwtProperties;
import com.ylab.intensive.util.PropertiesUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

@ApplicationScoped
public class CommonConfig {

    @Produces
    @Named("converter")
    public Converter converter() {
        return new Converter(new ObjectMapper().findAndRegisterModules());
    }

    @Produces
    @Named("jwtTokenService")
    public JwtTokenService jwtTokenService() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret(PropertiesUtil.get("jwt.secret"));
        jwtProperties.setAccess(Long.parseLong(PropertiesUtil.get("jwt.access")));
        jwtProperties.setRefresh(Long.parseLong(PropertiesUtil.get("jwt.refresh")));
        return new JwtTokenServiceImpl(jwtProperties);
    }

    @Produces
    @Named("securityAspect")
    public SecurityAspect securityAspect(ContextManager contextManager) {
        System.out.println("создали SecurityAspect");

        SecurityAspect securityAspect = SecurityAspect.aspectOf();
        securityAspect.inject(contextManager);
        return securityAspect;
    }

    @Produces
    @Named("auditAspect")
    public AuditAspect auditAspect(ContextManager contextManager, AuditDao auditDao, UserService userService) {
        System.out.println("создали AuditAspect");

        AuditAspect auditAspect = AuditAspect.aspectOf();
        auditAspect.inject(contextManager, auditDao, userService);
        return auditAspect;
    }
}
