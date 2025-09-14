# Taskflow API

API REST para gestão de usuários, tarefas e subtarefas com DDD + Arquitetura Hexagonal, observabilidade e qualidade de código.

![Build Status](https://github.com/rhenan35/taskflow-api/actions/workflows/build.yml/badge.svg)
![Qodana Code Quality](https://github.com/rhenan35/taskflow-api/actions/workflows/qodana_code_quality.yml/badge.svg)

## 🚀 Como Rodar o Projeto

### Com Docker (Recomendado)

1. **Clone o repositório:**
```bash
git clone <repository-url>
cd taskflow-api
```

2. **Execute com Docker Compose:**
```bash
docker-compose up -d
```

Isso irá subir:
- **PostgreSQL** na porta 5432
- **Jaeger UI** na porta 16686 (observabilidade)
- **API** na porta 8080

3. **Acesse a aplicação:**
- Dominio e porta: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- Jaeger: http://localhost:16686

### Desenvolvimento Local

```bash
# Compilar e testar
./mvnw clean verify

# Executar em modo desenvolvimento
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## 🔐 **Autenticação**

Para acessar os endpoints protegidos, utilize as credenciais fixas abaixo:

**Credenciais de Teste:**
- **Email:** `admin@taskflow.com`
- **Senha:** `taskflow123`

**Como obter o token:**
1. Faça uma requisição POST para `/api/auth/login` com as credenciais acima
2. Use o token retornado no header `Authorization: Bearer {token}` nas demais requisições

## 📋 **Endpoints Disponíveis**

### **Autenticação**
- `POST /api/auth/login` - Fazer login e obter token JWT

### **Tarefas**
- `GET /api/tarefas` - Listar tarefas com filtros e paginação
- `GET /api/tarefas/{id}` - Buscar tarefa por ID
- `POST /api/tarefas` - Criar nova tarefa
- `PUT /api/tarefas/{id}` - Atualizar tarefa
- `DELETE /api/tarefas/{id}` - Deletar tarefa
- `PATCH /api/tarefas/{id}/status` - Atualizar status da tarefa

### **Subtarefas**
- `GET /api/subtarefas` - Listar subtarefas com filtros e paginação
- `GET /api/subtarefas/{id}` - Buscar subtarefa por ID
- `POST /api/subtarefas` - Criar nova subtarefa
- `PUT /api/subtarefas/{id}` - Atualizar subtarefa
- `DELETE /api/subtarefas/{id}` - Deletar subtarefa
- `PATCH /api/subtarefas/{id}/status` - Atualizar status da subtarefa

## 🔧 **Exemplos de Uso (cURL)**

> **⚠️ IMPORTANTE:** Todos os endpoints protegidos requerem autenticação JWT. Primeiro faça login para obter o token:

**1. Fazer Login (obter token):**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@taskflow.com",
    "password": "taskflow123"
  }'
```

**2. Use o token retornado** no header `Authorization: Bearer {SEU_TOKEN}` nas requisições abaixo.

### **Usuários**

**Criar usuário:**
```bash
curl --request POST \
  --url http://localhost:8080/usuarios \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Content-Type: application/json' \
  --data '{
    "name": "Josh92",
    "email": "Fletcher_Ondricka55@yahoo.com"
  }'
```

**Consultar usuário por ID:**
```bash
curl --request GET \
  --url http://localhost:8080/usuarios/1e894ff8-39ec-42d1-a43a-97adc1612c85 \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}'
```

### **Tarefas**

**Criar tarefa:**
```bash
curl --request POST \
  --url http://localhost:8080/tarefas \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Content-Type: application/json' \
  --data '{
	"userId": "1e894ff8-39ec-42d1-a43a-97adc1612c85",
	"title": "Senior Marketing Supervisor",
	"description": "Clam vinitor supellex cui censura. Velit adsuesco caelestis stultus. Uterque audacia suffragium substantia tristis sto voluntarius."
}'
```

**Listar tarefas pendentes por status:**
```bash
curl --request GET \
  --url 'http://localhost:8080/tarefas?status=PENDING' \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}'
```

**Listar tarefas com filtros:**
```bash
curl --request GET \
  --url 'http://localhost:8080/tarefas/search?status=IN_PROGRESS&userId=uuid&title=projeto&createdAfter=2024-01-01T00%3A00%3A00&page=0&size=10&sortBy=createdAt&sortDirection=desc' \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Accept: application/json'
```

**Atualizar uma tarefa:**
```bash
curl --request PATCH \
  --url http://localhost:8080/tarefas/38467321-7573-477b-8695-a9f8d955e597/status \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Content-Type: application/json' \
  --data '{     "status": "IN_PROGRESS"   }'
```

### **Subtarefas**

**Criar uma subtarefa:**
```bash
curl --request POST \
  --url http://localhost:8080/tarefas/38467321-7573-477b-8695-a9f8d955e597/subtarefas \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Content-Type: application/json' \
  --data '{
	"taskId": "38467321-7573-477b-8695-a9f8d955e597",
	"title": "Lead Accountability Administrator",
	"description": "Umbra pectus ciminatio vos libero. Appositus vinitor abeo stipes itaque spoliatio. Sustineo apparatus sonitus et adfectus tergeo vilicus supra vester."
}'
```

**Listar subtarefas:**
```bash
curl --request GET \
  --url http://localhost:8080/tarefas/38467321-7573-477b-8695-a9f8d955e597/subtarefas \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}'
```

**Listar subtarefas com filtros:**
```bash
curl --request GET \
  --url 'http://localhost:8080/subtarefas/busca?status=PENDING&taskId=123e4567-e89b-12d3-a456-426614174000&title=exemplo&createdAfter=2024-01-01T00%3A00%3A00&createdBefore=2024-12-31T23%3A59%3A59&page=0&size=10&sort=createdAt&direction=desc' \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Accept: application/json' \
  --header 'Content-Type: application/json'
```

**Atualizar uma subtarefa:**
```bash
curl --request PATCH \
  --url http://localhost:8080/subtarefas/d0c87edf-714c-45cc-aef3-5b00dbf6c24e/status \
  --header 'Authorization: Bearer {SEU_TOKEN_JWT}' \
  --header 'Content-Type: application/json' \
  --data '{     "status": "IN_PROGRESS"   }'
```

## 🛠️ Tecnologias Utilizadas

### Core
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.5** - Framework principal
- **Maven** - Gerenciamento de dependências
- **PostgreSQL** - Banco de dados principal
- **H2** - Banco em memória para testes

### Arquitetura
- **DDD (Domain-Driven Design)** - Modelagem de domínio
- **Arquitetura Hexagonal** - Ports & Adapters
- **Spring Data JPA** - Persistência
- **Flyway** - Migração de banco

### Qualidade & Testes
- **JUnit 5** - Framework de testes
- **Mockito** - Mocks para testes
- **JaCoCo** - Cobertura de código (meta: 80%)
- **Qodana** - Análise estática de código
- **Lombok** - Redução de boilerplate

### Observabilidade
- **OpenTelemetry** - Instrumentação
- **Jaeger** - Distributed tracing
- **Zipkin** - Tracing alternativo
- **Spring Actuator** - Métricas e health checks

### Segurança & Documentação
- **Spring Security** - Autenticação Bearer Token
- **Springdoc OpenAPI** - Documentação automática
- **Docker & Docker Compose** - Containerização

### CI/CD
- **GitHub Actions** - Pipeline de build e qualidade
- **Testcontainers** - Testes de integração

## 🏗️ Arquitetura

```
src/main/java/com/rhenan/taskflow/
├── api/           # Controllers, configs, exception handlers
├── application/   # Use cases, DTOs, mappers
├── domain/        # Entidades, value objects, regras de negócio
└── infrastructure/ # Adaptadores, persistência, especificações
```

## 🔍 Regras de Negócio Implementadas

- **Tarefa só pode ser concluída se todas as subtarefas estiverem concluídas**
- **Transições de status controladas** (PENDING → IN_PROGRESS → COMPLETED)
- **Validações de domínio** em entidades e value objects
- **Não é possível adicionar subtarefas em tarefas concluídas**
