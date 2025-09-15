package com.rhenan.taskflow.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskflowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taskflow API")
                        .description("API REST para gestão de usuários, tarefas e subtarefas com DDD + Arquitetura Hexagonal")
                        .version("v1.0.0"));
    }
}