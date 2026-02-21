# EventHive – Event-Driven Order Processing Platform

A **Scalable Order Processing System** built with Spring Boot, Apache Pulsar, PostgreSQL, and Docker. This project demonstrates architect-level patterns: event-driven design, reliability (DLQ, retry, idempotency), and observability (health checks, graceful shutdown, distributed tracing readiness).

## Stack

- **Spring Boot 3.4**
- **Apache Pulsar** – messaging
- **PostgreSQL** – persistence (per-service DBs)
- **Docker + Docker Compose** – run everything locally

## Services

| Service             | Port | Responsibility                                      |
|---------------------|------|-----------------------------------------------------|
| **Order Service**   | 8081 | Create orders, idempotency, publish order/payment events |
| **Payment Service** | 8082 | Consume payment requests, DLQ, retry, publish payment-completed |
| **Inventory Service** | 8083 | Reserve inventory (Key_Shared + Shared subscriptions) |
| **Notification Service** | 8084 | Consume events, send (log) notifications           |

### API documentation (Swagger)

Order Service exposes **OpenAPI 3** docs and **Swagger UI**:

- **Swagger UI:** http://localhost:8081/swagger-ui.html  
- **OpenAPI JSON:** http://localhost:8081/v3/api-docs  

Use Swagger UI to try the *Create an order* endpoint with example payloads.

## Architect-Level Features

- **Key_Shared vs Shared subscription** – Inventory service uses both on `order-created` to compare ordering per key vs maximum throughput.
- **Dead Letter Topic** – Payment consumer uses Pulsar DLQ and retry letter topic after max redeliveries.
- **Retry with backoff** – Payment consumer uses `negativeAckRedeliveryDelay` and ack timeout for redelivery.
- **Idempotency** – Order and Payment services use `idempotencyKey` to avoid duplicate processing.
- **Distributed tracing** – Micrometer + OpenTelemetry on classpath; configure OTLP endpoint to enable.
- **Health checks + readiness** – All services expose `/actuator/health/liveness` and `/actuator/health/readiness`.
- **Graceful shutdown** – `server.shutdown=graceful` so in-flight requests and Pulsar consumers drain on SIGTERM.

## Quick Start

### Prerequisites

- Java 17+, Maven 3.9+
- Docker and Docker Compose

### Run with Docker Compose

```bash
docker compose up -d
```

Wait for Pulsar and Postgres to be healthy, then services will start. Create an order:

```bash
curl -s -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust-1",
    "idempotencyKey": "key-unique-123",
    "items": [
      { "productId": "11111111-1111-1111-1111-111111111111", "quantity": 2, "unitPrice": 10.50 }
    ]
  }'
```

Send the same request again with the same `idempotencyKey` – you get the same order (idempotent).

### Run locally (no Docker for apps)

1. Start infrastructure only:

   ```bash
   docker compose up -d pulsar postgres
   ```

2. Create DBs and users (first time): Postgres init script runs automatically; if you use a different Postgres, run `docker/init-db.sql` manually.

3. Run each service from the repo root:

   ```bash
   mvn -pl order-service -am spring-boot:run
   # In other terminals:
   mvn -pl payment-service -am spring-boot:run
   mvn -pl inventory-service -am spring-boot:run
   mvn -pl notification-service -am spring-boot:run
   ```

4. Order service: `http://localhost:8080` (or 8081 if you kept Compose ports). Defaults expect Postgres at `localhost:5432` and Pulsar at `localhost:6650`.

## Event Flow

1. **Order Service** – `POST /api/orders` creates order (idempotent by `idempotencyKey`), publishes:
   - `order-created` (OrderCreatedEvent)
   - `payment-requested` (PaymentRequestedEvent)
2. **Payment Service** – Consumes `payment-requested`, processes payment (idempotent), publishes `payment-completed`.
3. **Inventory Service** – Two subscriptions on `order-created`:
   - **Key_Shared** – ordering per orderId, scaled with concurrency.
   - **Shared** – round-robin, no ordering.
4. **Notification Service** – Consumes `order-created` and `payment-completed`, logs notifications.

## Configuration

- **Pulsar**: `spring.pulsar.client.service-url` (default `pulsar://localhost:6650`). Override with `PULSAR_HOST` in Docker.
- **Postgres**: `POSTGRES_HOST`, `POSTGRES_PORT`, `POSTGRES_USER`, `POSTGRES_PASSWORD` per service (see `application.yml` and Compose).
- **Tracing**: Add OTLP endpoint (e.g. `OTEL_EXPORTER_OTLP_ENDPOINT`) to enable distributed tracing.

## Project layout

```
EventHive/
├── pom.xml                 # Parent POM
├── docker-compose.yml
├── docker/
│   └── init-db.sql        # Postgres DBs and users
├── shared/                # Shared events and DTOs
├── order-service/
├── payment-service/
├── inventory-service/
└── notification-service/
```


