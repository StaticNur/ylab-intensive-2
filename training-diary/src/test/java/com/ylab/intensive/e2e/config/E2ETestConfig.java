package com.ylab.intensive.e2e.config;

import com.ylab.intensive.service.security.JwtTokenService;
import com.ylab.intensive.service.security.impl.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class E2ETestConfig {

    @Value("${spring.port}")
    private String port;

    @Bean
    public TestRestTemplate testRestTemplate(){
        return new TestRestTemplate(new RestTemplateBuilder()
        .rootUri("http://localhost:" + port));
    }

    @Bean
    public HeaderUtils headerUtils(JwtTokenService jwtTokenService, JwtUserDetailsService jwtUserDetailsService){
        return new HeaderUtils(jwtTokenService, jwtUserDetailsService);
    }
}
