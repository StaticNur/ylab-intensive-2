package io.ylab.auditspringbootstarter.config;

import io.ylab.auditspringbootstarter.advice.AuditAspect;
import io.ylab.auditspringbootstarter.service.AuditRepository;
import io.ylab.auditspringbootstarter.service.MessageBuilder;
import io.ylab.auditspringbootstarter.service.UserFinder;
import io.ylab.auditspringbootstarter.service.impl.MessageBuilderImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for AuditAutoConfig.
 * <p>
 * This class configures the auditing aspect and related beans based on the properties provided in
 * {@link AuditProperties}. It conditionally enables the aspect and auto-proxies.
 */
@Configuration
@EnableConfigurationProperties(AuditProperties.class)
@ConditionalOnProperty(prefix = "audit.aspect", name = "enabled", havingValue = "true")
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class AuditAutoConfig {

    private final UserFinder userFinder;
    private final AuditRepository auditRepository;

    /**
     * Creates an instance of {@link AuditAspect} configured with required beans.
     *
     * @param messageBuilder The message builder bean.
     * @return An instance of {@link AuditAspect}.
     */
    @Bean
    public AuditAspect auditAspect(MessageBuilder messageBuilder){
        return new AuditAspect(messageBuilder, auditRepository, userFinder);
    }

    /**
     * Creates a default {@link MessageBuilder} bean if none is provided.
     *
     * @return An instance of {@link MessageBuilder}.
     */
    @Bean
    @ConditionalOnMissingBean(MessageBuilder.class)
    public MessageBuilder messageBuilder(){
        return new MessageBuilderImpl();
    }
}
