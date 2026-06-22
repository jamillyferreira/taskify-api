package com.ferreira.taskify_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
                        )
                )
                .servers(List.of(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor Local"))
                );
    }
}
