package com.ylab.intensive.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up metrics.
 */
@Configuration
public class MetricConfig {
    /**
     * Creates a MeterBinder bean to bind metrics to the MeterRegistry.
     *
     * @return A MeterBinder bean for binding metrics.
     */
    @Bean
    MeterBinder meterBinder(){
        return meterRegistry -> {
            Counter.builder("greeting_count")
                    .description("Количество обращений")
                    .register(meterRegistry);
            Counter.builder("greeting_count_by_user")
                    .description("Количество обращений от пользователя")
                    .tag("email", "user")
                    .register(meterRegistry);
        };
    }
}
