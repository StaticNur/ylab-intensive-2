package io.ylab.loggingspringbootstarter.config;

import io.ylab.loggingspringbootstarter.advice.LoggingAspect;
import io.ylab.loggingspringbootstarter.advice.TimingAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfiguration {

    /**
     * Bean definition for the {@code LoggingAspect}.
     *
     * @return Instance of {@code LoggingAspect}
     */
    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    /**
     * Bean definition for the {@code TimingAspect}.
     *
     * @return Instance of {@code TimingAspect}
     */
    @Bean
    public TimingAspect timingAspect() {
        return new TimingAspect();
    }
}
