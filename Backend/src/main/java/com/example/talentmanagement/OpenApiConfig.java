package com.example.talentmanagement;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Talent Management Backend REST API")
                        .version("v1")
                        .description("Backend REST API for the Talent Management System.")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
