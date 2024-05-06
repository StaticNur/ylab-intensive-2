package com.ylab.intensive.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * Configuration class for Swagger 2 setup.
 * Configures Swagger 2 for generating API documentation for the REST services.
 */
@EnableSwagger2
@Configuration
public class Swagger2Config implements WebMvcConfigurer {

    /**
     * Adds resource handlers for Swagger UI.
     *
     * @param registry ResourceHandlerRegistry for configuring resource handlers
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/", "classpath:/META-INF/resources/webjars/");
    }

    /**
     * Configures the Docket bean for Swagger.
     *
     * @return Docket object for configuring Swagger API documentation
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ylab.intensive.in.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()));
    }

    /**
     * Retrieves API information for Swagger documentation.
     *
     * @return ApiInfo object containing API information
     */
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("Training diary Service REST API Documentation")
                .description("A workout diary application that will allow users to record their workouts, review them and analyze their workout progress.")
                .version("0.0.1")
                .build();
    }

    /**
     * Configures API key for Swagger authentication.
     *
     * @return ApiKey object representing the API key
     */
    private ApiKey apiKey() {
        return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, "header");
    }

    /**
     * Configures security context for Swagger.
     *
     * @return SecurityContext object representing the security context
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/"))
                .build();
    }

    /**
     * Defines default authentication for Swagger.
     *
     * @return List of SecurityReference objects representing default authentication
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(
                new SecurityReference("JWT", authorizationScopes));
    }
}
