# E-Commerce Backend - Development Guide

## Project Structure

```
e-commerce/
├── src/main/java/com/app/
│   ├── config/           # Spring configurations (Security, JWT, MinIO, etc.)
│   ├── controller/       # REST API endpoints
│   ├── exception/        # Custom exceptions and error handlers
│   ├── model/            # Entities, DTOs, Request/Response models
│   ├── repository/       # JPA Repositories
│   ├── security/         # JWT utilities
│   └── service/          # Business logic
├── .github/
│   └── workflows/        # GitHub Actions CI/CD
├── devops/               # Docker Compose files
├── postman/              # API test collection
└── docs/                 # Documentation and diagrams
```

## Tech Stack

- **Java 25**
- **Spring Boot 4.0.3**
- **PostgreSQL 17**
- **Spring Security** (JWT authentication)
- **Spring Data JPA**
- **MapStruct 1.6.3**
- **Lombok**
- **MinIO** (Development) / **AWS S3** (Production)
- **Maven Wrapper (3.9.12)**

## Getting Started

### 1. Prerequisites
- Java 25
- Docker & Docker Compose
- PostgreSQL client (optional)

### 2. Database & MinIO Setup

```bash
# Start PostgreSQL and MinIO containers
docker-compose up -d

# Check status
docker-compose ps
```

### 3. Run Application

```bash
# Return to project root
cd ..

# Install dependencies and build
.\mvnw.cmd clean install

# Start application
.\mvnw.cmd spring-boot:run
```

**Alternative (with JAR):**
```bash
java -jar target/e-commerce-0.0.1-SNAPSHOT.jar
```

## API Testing

### Postman Collection
Import `postman/e-commerce.postman_collection.json` into Postman.

### Default Endpoints
```
API:           http://localhost:8080
MinIO Console: http://localhost:9001
```

### MinIO Console Credentials
```
User:     admin
Password: admin123
```

## Branch Structure

```
main              → production, always working
develop           → main development branch
feature/EAD-xx    → new feature
fix/EAD-xx        → bug fix
hotfix/EAD-xx     → urgent production fix
refactor/EAD-xx   → code restructure
```

## Commit Message Format

```
feat(scope): what was done
fix(scope): what was fixed
refactor(scope): what was restructured
chore(scope): dependency or config change
docs(scope): documentation change
test(scope): test added
```

**Examples:**
```
feat(auth): JWT login endpoint added
feat(product): product CRUD endpoints added
fix(order): order total price calculation fixed
chore(deps): mapstruct dependency added
```

## PR Rules

- One PR = one feature or one fix
- Always branch from develop
- Title must follow commit format
- Add "Closes #issue_number" in description
- Merge to main only when project is complete

## Useful Commands

```bash
# Build without tests
.\mvnw.cmd clean install -DskipTests

# Run tests
.\mvnw.cmd test

# Stop containers
docker-compose -f devops/docker-compose.yml down

# Clean docker volumes
docker-compose -f devops/docker-compose.yml down -v

# View logs
docker-compose -f devops/docker-compose.yml logs -f
```

## Configuration

- Default port: `8080`
- Database port: `5432`
- MinIO API port: `9000`
- MinIO Console port: `9001`
