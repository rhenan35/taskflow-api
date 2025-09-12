package com.rhenan.taskflow.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Rhenan Coelho")
                                .email("rhenan.coelho@example.com")
                                .url("https://github.com/rhenan35/taskflow-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}