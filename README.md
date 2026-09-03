# E-commerce Backend

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-4169E1?logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-Cache-DC382D?logo=redis&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-Payment-635BFF?logo=stripe&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Auth-000000?logo=jsonwebtokens&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)
![Flyway](https://img.shields.io/badge/Flyway-Migration-CC0200?logo=flyway&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?logo=hibernate&logoColor=white)

A RESTful e-commerce backend built with Spring Boot, focused on clean business flows, security, payment reliability, and database consistency.

## Tech Stack

| Technology | Purpose |
| :--- | :--- |
| **Java 26** | Core Programming Language |
| **Spring Boot 4.1** | Application Framework |
| **Spring Data JPA / Hibernate** | ORM & Database Communication |
| **PostgreSQL** | Primary Relational Database |
| **Redis** | In-Memory Data Store & Caching |
| **Flyway** | Database Schema Migration |
| **Spring Security / JWT** | Authentication & Role-based Authorization |
| **Stripe** | Payment Gateway & Webhook Integration |
| **Maven** | Dependency Management & Build Tool |
| **Docker / Docker Compose** | Containerization & Local Infrastructure |

## Current Features

### Authentication & Authorization

- User registration with BCrypt password hashing
- JWT access token authentication
- Refresh token support with database persistence
- Role-based authorization: `USER` / `ADMIN`
- Logout / refresh-token revocation
- Global exception handling and standardized API errors

### Product

- Product CRUD
- Product status management
- Role-based access:
  - `USER`: only active products
  - `ADMIN`: all products and product management
- Pagination and dynamic filtering
- Optimistic locking with JPA `@Version` to protect stock updates from concurrent requests

### Cart

- Get current user's cart
- Add product to cart
- Update quantity
- Remove item
- Clear cart
- Stock validation

### Order

- Create order from cart
- Create order items with product/price snapshots
- Calculate order total
- Decrease product stock
- Automatically clear cart after successful order creation
- Retrieve user's orders and order details
- Transactional order creation
- Optimistic locking to prevent overselling during concurrent orders

### Payment

- Stripe Checkout integration
- Card payments through Stripe
- Stripe Idempotency-Key
- Application-level API idempotency
- PostgreSQL `UNIQUE` constraint for concurrent idempotency requests
- Stripe webhook handling
- Webhook-safe payment status updates
- Payment status flow including:
  - `PENDING`
  - `PAID`
  - `CANCELLED`
  - `REFUNDED`
- Stripe refund support
- Provider payment ID received from Stripe webhook

## Reliability & Concurrency

The payment and order flows use multiple layers of protection:

```text
Client
  |
  +-- API Idempotency-Key
  |
  v
PostgreSQL UNIQUE constraint
  |
  v
Stripe Idempotency-Key
  |
  v
Stripe Webhook
  |
  v
Webhook Idempotency
```

For inventory concurrency:

```text
Concurrent Order Requests
          |
          v
      @Version
          |
          v
Hibernate Optimistic Locking
          |
     +----+----+
     |         |
  Success    Conflict
     |         |
  Commit    Rollback
```

The optimistic-locking flow has been tested with concurrent order requests.

## Database & Migration

Database schema is managed with Flyway.

Main tables include:

- `users`
- `refresh_tokens`
- `products`
- `carts`
- `cart_items`
- `orders`
- `order_items`
- `payments`
- `idempotency_records`

JPA uses:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

so Hibernate validates the schema while Flyway owns schema migrations.

## Architecture

The project follows a layered Spring Boot architecture:

```text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
PostgreSQL
```

Cross-cutting concerns such as security, exception handling, idempotency, and payment webhooks are separated into dedicated components.

## Project Status

### Completed

- Authentication & authorization
- Product management
- Cart management
- Order creation and retrieval
- Stock validation
- Optimistic locking
- Stripe Checkout
- Payment webhook handling
- Payment refund
- API idempotency
- Stripe idempotency
- Refresh-token / logout flow
- Pagination and dynamic filtering

### Next

- Improve Order status management
- Order cancellation and stock restoration
- Further validation and edge-case testing
- Automated tests for concurrency and payment flows
- API documentation and final cleanup

## Running Locally

### Requirements

- JDK 26
- Maven
- Docker Desktop
- PostgreSQL / Docker
- Redis / Docker
- Stripe CLI for local webhook testing

Start infrastructure with Docker Compose, configure application properties, then run:

```bash
./mvnw spring-boot:run
```

For Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

For local Stripe webhook forwarding:

```bash
stripe listen --forward-to localhost:8080/api/webhooks/stripe
```

> Never commit Stripe secret keys, webhook secrets, JWT secrets, database credentials, or other sensitive configuration to the repository.
