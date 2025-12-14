package com.team3.notificationservice.config;

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
        // 1. 자물쇠 '부품' 정의 (이름을 간단하게 "JWT"로 설정)
        String jwtSchemeName = "JWT";

        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        // 2. 서버 정보 설정 (Gateway 주소)
        Server gatewayServer = new Server()
                .url("http://localhost:8000/api/v1/notification-service")
                .description("Gateway Server");

        return new OpenAPI()
                .info(new Info().title("Notification Service API").version("v1.0.0"))
                .addServersItem(gatewayServer) // Gateway 주소 등록
                .components(components);
        // ❌ .addSecurityItem(...) 삭제! (이게 있으면 모든 곳에 다 걸림)
    }
}
