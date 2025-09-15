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

### **Users**
- `POST /users` - Create new user
- `GET /users/{id}` - Find user by ID

### **Tasks**
- `GET /tasks?status={status}` - List tasks by status
- `GET /tasks/search` - Search tasks with filters and pagination
- `POST /tasks` - Create new task
- `PATCH /tasks/{id}/status` - Update task status
- `POST /tasks/{taskId}/subtasks` - Create subtask for a task
- `GET /tasks/{taskId}/subtasks` - List subtasks of a task

### **Subtasks**
- `GET /subtasks/search` - Search subtasks with filters and pagination
- `PATCH /subtasks/{id}/status` - Update subtask status

## 🔧 **Exemplos de Uso (cURL)**

> **⚠️ IMPORTANTE:** Todos os endpoints protegidos requerem autenticação JWT. Primeiro faça login para obter o token:

**1. Fazer Login (obter token):**
```bash
curl --request POST \
  --url http://localhost:8080/api/auth/login \
  --header 'Content-Type: application/json' \
  --data '{
	"email": "admin@taskflow.com",
	"password": "taskflow123"
}'
```

**2. Use o token retornado** no header `Authorization: Bearer {SEU_TOKEN}` nas requisições abaixo.

### **Users**

**Create user:**
```bash
curl --request POST \
  --url http://localhost:8080/users \
  --header 'Authorization: Bearer {token}' \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "Gwen87", 
	"email": "Keeley9@yahoo.com"
}'
```

**Find user by ID:**
```bash
curl --request GET \
  --url http://localhost:8080/users/94c03f91-2465-4565-913d-197962232e35 \
  --header 'Authorization: Bearer {token}'
```

### **Tasks**

**Create task:**
```bash
curl --request POST \
  --url http://localhost:8080/tasks \
  --header 'Authorization: Bearer {token}' \
  --header 'Content-Type: application/json' \
  --data '{
	"userId": "94c03f91-2465-4565-913d-197962232e35",
	"title": "Direct Mobility Planner",
	"description": "Cetera vicissitudo cultura.
Tremo truculenter sub."
}'
```

**List tasks by status:**
```bash
curl --request GET \
  --url 'http://localhost:8080/tasks?status=PENDING' \
  --header 'Authorization: Bearer {token}'
```

**Search tasks with filters:**
```bash
curl --request GET \
  --url 'http://localhost:8080/tasks/search?status=PENDING&userId=uuid&title=projeto&createdAfter=2024-01-01T00%3A00%3A00&page=0&size=10&sortBy=createdAt&sortDirection=desc' \
  --header 'Accept: application/json' \
  --header 'Authorization: Bearer {token}'
```

**Update task status:**
```bash
curl --request PATCH \
  --url http://localhost:8080/tasks/38467321-7573-477b-8695-a9f8d955e597/status \
  --header 'Authorization: Bearer {token}' \
  --header 'Content-Type: application/json' \
  --data '{"status": "IN_PROGRESS"}'
```

### **Subtasks**

**Create a subtask:**
```bash
curl --request POST \
  --url http://localhost:8080/tasks/38467321-7573-477b-8695-a9f8d955e597/subtasks \
  --header 'Authorization: Bearer {token}' \
  --header 'Content-Type: application/json' \
  --data '{
	"taskId": "38467321-7573-477b-8695-a9f8d955e597",
	"title": "Internal Program Facilitator",
	"description": "Usque sunt conventus ulciscor amitto amicitia nobis crepusculum acidus. Tondeo correptius suus ait. Coaegresco vis creta."
}'
```

**List subtasks:**
```bash
curl --request GET \
  --url http://localhost:8080/tasks/38467321-7573-477b-8695-a9f8d955e597/subtasks \
  --header 'Authorization: Bearer {token}'
```

**Search subtasks with filters:**
```bash
curl --request GET \
  --url 'http://localhost:8080/subtasks/search?status=PENDING&taskId=123e4567-e89b-12d3-a456-426614174000&title=exemplo&createdAfter=2024-01-01T00%3A00%3A00&createdBefore=2024-12-31T23%3A59%3A59&page=0&size=10&sort=createdAt&direction=desc' \
  --header 'Accept: application/json' \
  --header 'Authorization: Bearer {token}' \
  --header 'Content-Type: application/json'
```

**Update subtask status:**
```bash
curl --request PATCH \
  --url http://localhost:8080/subtasks/d0c87edf-714c-45cc-aef3-5b00dbf6c24e/status \
  --header 'Authorization: Bearer {token}' \
  --header 'Content-Type: application/json' \
  --data '{"status": "IN_PROGRESS"}'
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
