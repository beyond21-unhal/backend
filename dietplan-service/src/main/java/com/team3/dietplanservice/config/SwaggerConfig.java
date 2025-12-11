package com.team3.dietplanservice.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        String jwtSchemeName = "JWT";

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));


        Server gatewayServer = new Server()
                .url("http://localhost:8000/api/v1/dietplan-service")
                .description("Gateway Server");

        return new OpenAPI()
                .info(new Info().title("Diet Plan API").version("v1.0.0"))
                .addServersItem(gatewayServer)
                .components(components);

    }
}

