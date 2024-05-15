package io.ylab.auditspringbootstarter.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "audit.aspect")
public class AuditProperties {

    /**
     * Flag to enable common logging AOP on the service layer.
     * Defaults to true.
     */
    private boolean enabled = true;
}
