# Gym Management System - Multi-Tenant SaaS Backend

A production-ready Spring Boot backend for a multi-tenant Gym Management System with JWT authentication and role-based authorization.

## Architecture Overview

### Multi-Tenancy Strategy

This application uses **PostgreSQL schema-based multi-tenancy**, where:
- Each gym (tenant) has its own dedicated schema in the same database
- The `public` schema contains global tables: `users`, `gyms`, and `refresh_tokens`
- All tenant-specific tables (members, payments, routines, etc.) are isolated per schema

### Tenant Resolution

Tenants are resolved per HTTP request using the `X-Tenant-ID` header:

```
X-Tenant-ID: gym_abc123
```

The `TenantInterceptor` extracts this header and stores it in a `ThreadLocal` via `TenantContext`. Hibernate's `CurrentTenantIdentifierResolver` reads from this context to determine which schema to use for the current request.

### Authentication Flow

1. **Login**: POST `/api/auth/login` with email/password
   - Returns access token (15 min) and refresh token (7 days)
   - User data is stored in the `public` schema

2. **Protected Requests**: Include `Authorization: Bearer <access-token>` header
   - JWT is validated by `JwtAuthenticationFilter`
   - User email and role are extracted from the token

3. **Refresh Token**: POST `/api/auth/refresh` with refresh token
   - Returns new access token (refresh token remains valid)

4. **Logout**: POST `/api/auth/logout` with refresh token
   - Marks refresh token as revoked in database

### Role-Based Authorization

- **ADMIN**: Full access to all gym data (members, trainers, products, payments)
- **TRAINER**: Can manage routines and exercises for assigned members
- **MEMBER**: Can only view their own routines, workouts, and progress

## Tech Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **PostgreSQL** (multi-tenant with schema isolation)
- **Spring Security** with JWT
- **Hibernate/JPA** with multi-tenancy support
- **Gradle** (Kotlin DSL)
- **Lombok** for boilerplate reduction
- **Jakarta Validation** for DTO validation

## Project Structure

```
src/main/java/com/gym/
├── config/                    # Security, multi-tenancy, web configuration
├── controller/                # REST API endpoints
├── dto/
│   ├── request/              # Request DTOs
│   └── response/             # Response DTOs
├── entity/                   # JPA entities
├── enums/                    # Enums (Role, MemberStatus, PaymentStatus)
├── exception/                # Custom exceptions and global handler
├── repository/               # JPA repositories
├── security/                 # JWT utilities and filters
├── service/
│   └── impl/                 # Service implementations
└── util/                     # Tenant context and utilities
```

## Database Setup

### 1. Create Database

```sql
CREATE DATABASE gym_saas;
```

### 2. Create Public Schema Tables

The `public` schema will be auto-created by Hibernate. It contains:
- `users` - All user accounts (ADMIN, TRAINER, MEMBER)
- `gyms` - Gym/tenant metadata
- `refresh_tokens` - JWT refresh tokens

### 3. Create Tenant Schemas

For each new gym, create a schema:

```sql
CREATE SCHEMA gym_abc123;
```

Hibernate will auto-create tables in the tenant schema when accessed for the first time.

## Running the Application

### Prerequisites

- Java 17+
- PostgreSQL 14+
- Gradle 8+

### Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gym_saas
    username: your_username
    password: your_password

jwt:
  secret: your-256-bit-secret-key-change-this-in-production
  access-token-expiration: 900000  # 15 minutes
  refresh-token-expiration: 604800000  # 7 days
```

### Build and Run

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The server will start on `http://localhost:8080`

## API Endpoints

### Authentication (Public)

- `POST /api/auth/login` - Login with email/password
- `POST /api/auth/refresh` - Refresh access token
- `POST /api/auth/logout` - Logout and revoke refresh token

### Admin Endpoints (ADMIN only)

**Members:**
- `POST /api/admin/members` - Create member
- `GET /api/admin/members` - List all members (paginated)
- `GET /api/admin/members/{id}` - Get member by ID
- `PUT /api/admin/members/{id}` - Update member
- `DELETE /api/admin/members/{id}` - Delete member

**Products:**
- `POST /api/admin/products` - Create product
- `GET /api/admin/products` - List all products (paginated)
- `GET /api/admin/products/{id}` - Get product by ID
- `PUT /api/admin/products/{id}` - Update product
- `DELETE /api/admin/products/{id}` - Delete product

### Trainer Endpoints (ADMIN + TRAINER)

- `POST /api/trainer/routines` - Create routine for member
- `GET /api/trainer/routines/member/{memberId}` - Get routines for member
- `GET /api/trainer/routines/{id}` - Get routine by ID
- `DELETE /api/trainer/routines/{id}` - Delete routine

### Member Endpoints (All roles)

- `GET /api/member/routines` - Get my routines
- `GET /api/member/routines/{id}` - Get routine details

## Request Examples

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@gym.com",
    "password": "password123"
  }'
```

### Create Member (ADMIN)

```bash
curl -X POST http://localhost:8080/api/admin/members \
  -H "Authorization: Bearer <access-token>" \
  -H "X-Tenant-ID: gym_abc123" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "+1234567890",
    "membershipType": "Premium",
    "joinDate": "2025-01-01",
    "status": "ACTIVE"
  }'
```

## Security Features

- **BCrypt** password hashing
- **JWT** with access + refresh token pattern
- **Stateless** authentication (no sessions)
- **Role-based** access control with Spring Security
- **Schema-based** data isolation per tenant
- **Input validation** using Jakarta Validation
- **Global exception** handling with meaningful HTTP status codes

## Future Enhancements

- Add OAuth2 social login
- Implement member self-registration
- Add workout logging endpoints
- Implement progress tracking with photos
- Add payment processing integration
- Email notifications for membership expiry
- Implement trainer assignment logic
- Add audit logging for tenant operations

## License

MIT
