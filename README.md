# Taskflow API

API REST para gestão de usuários, tarefas e subtarefas, com regras de negócio, validações e observabilidade. Desenvolvida em Java 21 com Spring Boot, DDD + Hexagonal, CI/CD, testes automatizados e documentação OpenAPI.

![Build Status](https://github.com/rhenan35/taskflow-api/actions/workflows/build.yml/badge.svg)
![Qodana Code Quality](https://github.com/rhenan35/taskflow-api/actions/workflows/qodana_code_quality.yml/badge.svg)

## Tecnologias

- Java 21
- Spring Boot
- Maven
- Flyway
- Springdoc OpenAPI
- H2 / PostgreSQL
- JUnit5
- JaCoCo
- GitHub Actions
- Qodana

## Estrutura de Pacotes

- `api`
- `application`
- `domain`
- `infrastructure`

## Como rodar

Execute os seguintes comandos para compilar e testar o projeto:

```bash
mvn clean verify
```

Utilize os perfis `dev`, `local` ou `test` conforme necessário para diferentes ambientes.

## Documentação API

A documentação da API está disponível via Swagger UI em:

```
/swagger-ui.html
```
