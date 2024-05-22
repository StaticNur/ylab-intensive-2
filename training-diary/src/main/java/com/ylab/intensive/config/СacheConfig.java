package com.ylab.intensive.config;

import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class СacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        return new CaffeineCacheManager("roleId", "AllUser", "getInfoByWorkoutId",
                "viewHistoryWorkouts", "findWorkoutTypesByUserId");
    }
}
