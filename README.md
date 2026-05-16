# SmartShop

Production-style e-commerce backend application built with Spring Boot.

## Features

- JWT Authentication & Authorization
- Role-based access control (`USER`, `ADMIN`, `SUPER_ADMIN`)
- Product management
- Order & OrderItem system
- Balance payment system
- Product stock validation
- Duplicate product detection in orders
- Redis caching
- Global exception handling
- Swagger/OpenAPI documentation
- Pagination & filtering with Specification API
- Liquibase database migrations
- Docker & Docker Compose support
- PostgreSQL integration
- Soft delete support
- Validation handling

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- PostgreSQL
- Redis
- Liquibase
- Docker
- Swagger/OpenAPI
- Maven/Gradle

---

## Project Structure

```text
src/main/java/com/example/smartshop
│
├── config
├── controller
├── dto
├── exception
├── model
├── repository
├── service
├── specification
└── mapper


Security
The application uses JWT-based authentication and role-based authorization.

Roles
USER
ADMIN
SUPER_ADMIN
Example Protected Endpoints
Endpoint	Access
/api/v1/products/**	Public
/api/v1/orders/**	Authenticated Users
/api/v1/users/admin	SUPER_ADMIN
/api/v1/users/**	ADMIN



Swagger
Swagger UI:
http://localhost:8077/swagger-ui/index.html


Docker
Run services:
docker compose up -d

Services:
PostgreSQL
Redis
Adminer


Environment Variables
DB_PASSWORD=your_password
JWT_SECRET=your_secret


Business Logic
Order Creation
Checks product availability
Prevents duplicate products in order
Calculates total price
Validates user balance
Reduces product quantity
Deducts money from user balance


Order Cancel
Restores product quantities
Returns balance to user
Changes status to CANCELLED


Database
Liquibase is used for:
table creation
constraints
foreign keys
indexes
seed data

Author
Rasul Kerimov
