package com.authrbac.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// This configures Swagger UI so we can test our protected endpoints.
// The @SecurityScheme annotation tells Swagger that our API uses
// "Bearer token" authentication - adds an "Authorize" button to Swagger UI.
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Auth & RBAC API",
                version = "1.0",
                description = "JWT Authentication + Role-Based Access Control with Spring Boot"
        )
)
@SecurityScheme(
        name = "bearerAuth",           // this name is referenced in our controllers
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // No beans needed here. Annotations do all the work.
}
