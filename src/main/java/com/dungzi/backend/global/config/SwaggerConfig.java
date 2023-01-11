package com.dungzi.backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    public static final String TITLE="Dunji-Rest API";
    public static final String DESCRIPTION="Dunji API명세서";
    public static final String VERSION="v0.0.1";

    @Bean
    public GroupedOpenApi test() {
        return GroupedOpenApi.builder()
                .group("test")
                .pathsToMatch("/api/test/**")
                .build();
    }
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION));
    }
}
