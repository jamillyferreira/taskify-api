package com.ferreira.taskify_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taskify API")
                        .description("API para gerenciamento de tarefas com autenticação JWT")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Jamilly Ferreira")
                                .email("jamillyferreira.dev@gmail.com")
                                .url("https://github.com/jamillyferreira")
                        ))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor Local"),
                        new Server()
                                .url("https://taskify-api-jiws.onrender.com")
                                .description("Servidor em Produção")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtido no endpoint /api/auth/login")
                        ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
