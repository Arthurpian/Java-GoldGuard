# Bet Ledger — Spring Boot 4 + MySQL (Maven/Java 21)

Sistema de extratos para depósitos, saques e apostas (por casa), com métricas e alertas para ajudar no controle de gastos.

## 👥 Integrantes
- **Márcio Gastaldi** — RM98811  
- **Arthur Bessa Pian** — RM99215  
- **Davi Desenzi** — RM550849  
- **João Victor** — RM551410  

---

## 🎯 Objetivo
Registrar **depósitos**, **saques** e **apostas** por **casa de aposta**, calcular **métricas** (totais, por período, por casa) e emitir **alertas** quando os gastos mensais se aproximarem/ultrapassarem o orçamento definido pelo usuário.

---

## 🧱 Arquitetura (camadas)  
`controller → service → repository → domain (entity/vo/enums) → dto → exception`  

```
bet-ledger/
├─ pom.xml
├─ README.md
├─ docs/samples/
│  ├─ create-user.json
│  ├─ create-deposit.json
│  ├─ create-bet.json
│  └─ metrics-summary.http
└─ src/main/
   ├─ java/com/gastaldl/betledger/
   │  ├─ BetLedgerApplication.java
   │  ├─ controller/
   │  │  ├─ UserController.java
   │  │  ├─ TransactionController.java
   │  │  └─ MetricsController.java
   │  ├─ service/
   │  │  ├─ UserService.java
   │  │  ├─ TransactionService.java
   │  │  └─ MetricsService.java
   │  ├─ repository/
   │  │  ├─ UserRepository.java
   │  │  └─ TransactionRepository.java
   │  ├─ domain/
   │  │  ├─ entity/ (User.java, Transaction.java)
   │  │  ├─ enums/ (TransactionKind.java)
   │  │  └─ vo/ (Money.java)
   │  ├─ dto/
   │  │  ├─ CreateUserRequest.java, UserResponse.java
   │  │  ├─ CreateTransactionRequest.java, TransactionResponse.java
   │  │  └─ MetricsSummaryResponse.java
   │  └─ exception/
   │     ├─ ApiError.java
   │     ├─ BusinessException.java
   │     ├─ NotFoundException.java
   │     └─ RestExceptionHandler.java
   └─ resources/
      ├─ application.yml
      └─ db/migration/V1__baseline.sql
```

---

## 🧪 Entidades e Relacionamentos
**User**  
`id, name, email(unique), monthlyBudget, alertThresholdPercent, createdAt`

**Transaction**  
`id, user_id(FK), kind[DEPOSIT|WITHDRAWAL|BET], amount>0, bettingHouse, occurredAt, note, createdAt`

**Índices**
- `transactions (user_id, occurred_at)` para filtragem por período  
- `transactions (kind)`  
- `transactions (betting_house)`  

---

## 🧰 Stack
- **Java 21**
- **Spring Boot 4.0.0-M3** (Milestone)
- Spring Web, Spring Data JPA, Validation, Lombok
- **MySQL 8** (+ Flyway para migrações)
- Testes: Spring Boot Starter Test

---

## 📦 Pré-requisitos
- Java 21
- MySQL 8
- Maven (ou `./mvnw`)
- Cliente HTTP (Insomnia/Postman/curl)

---

## 🗄️ Banco & Migrações
Crie o schema:
```sql
CREATE DATABASE bet_ledger CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```
O **Flyway** executa `V1__baseline.sql` automaticamente ao subir a aplicação.

---

## ⚙️ Configuração (`src/main/resources/application.yml`)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bet_ledger?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
    username: root
    password: root
  jpa:
    open-in-view: false
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
        jdbc.time_zone: America/Sao_Paulo
  flyway:
    enabled: true
    locations: classpath:db/migration
    baseline-on-migrate: true
    schemas: bet_ledger

server:
  port: 8080
```

> Ajuste `username/password` conforme seu ambiente. Ative o **annotation processing** do Lombok no IntelliJ (Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable).

---

## 🚀 Executando
```bash
# no diretório do projeto
./mvnw spring-boot:run
# ou
mvn spring-boot:run
```
A API sobe em `http://localhost:8080`.

---

## 🔗 Endpoints

| Método | Caminho                                        | Descrição                                      |
|-------:|------------------------------------------------|------------------------------------------------|
| POST   | `/api/v1/users`                                | Criar usuário                                  |
| POST   | `/api/v1/transactions`                         | Criar transação (DEPOSIT/WITHDRAWAL/BET)       |
| GET    | `/api/v1/transactions?userId=&from=&to=`       | Listar transações (por usuário/período)        |
| GET    | `/api/v1/transactions/{id}`                    | Detalhar transação                             |
| DELETE | `/api/v1/transactions/{id}`                    | Remover transação                              |
| GET    | `/api/v1/metrics/summary?userId=&from=&to=`    | Resumo de métricas e alerta                    |

### Exemplos (Insomnia/Postman/curl)

**Criar usuário**
```http
POST http://localhost:8080/api/v1/users
Content-Type: application/json

{
  "name": "Márcio Gastaldi",
  "email": "marcio@example.com",
  "monthlyBudget": 2000.00,
  "alertThresholdPercent": 30
}
```

**Criar transação — depósito**
```http
POST http://localhost:8080/api/v1/transactions
Content-Type: application/json

{
  "userId": 1,
  "kind": "DEPOSIT",
  "amount": 500.00,
  "bettingHouse": null,
  "occurredAt": "2025-09-21T10:30:00",
  "note": "Depósito PIX"
}
```

**Criar transação — aposta/saque em casa**
```http
POST http://localhost:8080/api/v1/transactions
Content-Type: application/json

{
  "userId": 1,
  "kind": "BET",
  "amount": 120.00,
  "bettingHouse": "Bet365",
  "occurredAt": "2025-09-21T11:10:00",
  "note": "Cupom 1289"
}
```

**Listar por período**
```http
GET http://localhost:8080/api/v1/transactions?userId=1&from=2025-09-01T00:00:00&to=2025-09-30T23:59:59
```

**Resumo de métricas**
```http
GET http://localhost:8080/api/v1/metrics/summary?userId=1&from=2025-09-01T00:00:00&to=2025-09-30T23:59:59
```

Resposta (exemplo):
```json
{
  "totalDeposits": 500.00,
  "totalWithdrawals": 0.00,
  "totalBets": 120.00,
  "net": 380.00,
  "spentThisMonth": 120.00,
  "monthlyBudget": 2000.00,
  "alertThresholdPercent": 30,
  "riskLevel": "LOW",
  "byBettingHouse": { "Bet365": 120.00 }
}
```

---

## ✅ Validações & Erros
- Validação com **Jakarta Validation** nos DTOs (@NotBlank, @Email, @DecimalMin, etc.).
- Tratamento centralizado via `@RestControllerAdvice`.

Formato de erro:
```json
{
  "message": "Transação não encontrada: 99",
  "path": "/api/v1/transactions/99",
  "timestamp": "2025-09-21T15:47:10.123Z"
}
```

---

## 📊 Métricas & Alerta
- **spentThisMonth**: soma de `WITHDRAWAL + BET` no mês corrente.  
- **riskLevel**:
  - `HIGH`  → quando `% gasto >= alertThresholdPercent`
  - `MEDIUM` → quando próximo do limite (ex.: entre `threshold-10` e `threshold`)
  - `LOW`   → abaixo da faixa de atenção  
- **byBettingHouse**: total gasto por casa no período consultado.

---

## 📝 Critérios de Avaliação (como atendemos)
- **Estruturação (25%)**: camadas separadas, DTO/VO/Enums, código limpo.
- **Requisições (20%)**: endpoints REST completos, validação de entrada, `ResponseEntity`, handler de erros.
- **Banco (20%)**: modelagem com FK/índices, migrações Flyway, CRUD funcional.
- **Interface de acesso (15%)**: testável via Insomnia/Postman/curl.
- **Documentação (10%)**: este README com setup, exemplos, tecnologias.

---

## 🧭 Tecnologias
Java 21 · Spring Boot 4.0.0-M3 · Web · Data JPA · Validation · Lombok · Flyway · MySQL 8

---

## 🧪 Dicas de Teste Rápido (curl)
```bash
curl -X POST http://localhost:8080/api/v1/users \
 -H "Content-Type: application/json" \
 -d '{"name":"Teste","email":"t@e.com","monthlyBudget":1500,"alertThresholdPercent":30}'

curl -X POST http://localhost:8080/api/v1/transactions \
 -H "Content-Type: application/json" \
 -d '{"userId":1,"kind":"BET","amount":100,"bettingHouse":"Bet365","occurredAt":"2025-09-21T12:00:00","note":"teste"}'

curl "http://localhost:8080/api/v1/metrics/summary?userId=1"
```

---

## 🗺️ Roadmap (ideias futuras)
- Autenticação/JWT e multiusuário real
- Exportação CSV/Excel do extrato
- Métricas avançadas (média móvel, limites por casa)
- Notificações (e-mail/Telegram) quando risco = HIGH
- Paginação e filtros adicionais nos endpoints de transações

---
