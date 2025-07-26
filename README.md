# 📄 Document Access Control System (Spring Boot + JWT + RBAC)

A secure document management REST API built with **Spring Boot**, featuring:
- ✅ Role-Based Access Control (`ADMIN`, `USER`)
- ✅ Fine-grained Permissions (READ, WRITE, DELETE)
- 🔐 JWT Authentication
- 🚀 Docker + PostgreSQL
- 🧪 Postman Collection
- 🎯 Soft Delete with Hibernate

---

## 🚀 Features

- **User Auth**: Register & login with JWT token
- **Document CRUD** (Admin only for create/delete)
- **Grant Permissions**: Admin/owners can grant READ, WRITE, DELETE to others
- **Access Check**: Batch check permission access
- **Soft Delete**: Documents are marked as deleted, not removed
- **Swagger UI**: Interactive API docs

---

## 👥 Sample Users

| Username | Password   | Role  |
|----------|------------|-------|
| `admin`  | `admin123` | ADMIN |
| `user1`  | `user123`  | USER  |

---

## 🔐 Auth: JWT

Use `/auth/login` to obtain a JWT token.

```
POST /auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

Use token in headers:

```
Authorization: Bearer <your_token>
```

---

## 📬 Endpoints Overview

| Method | Endpoint                      | Description                      | Role Required |
|--------|-------------------------------|----------------------------------|---------------|
| POST   | `/auth/register`              | Register user                    | Public        |
| POST   | `/auth/login`                 | Login + get JWT                  | Public        |
| POST   | `/documents`                  | Create document                  | ADMIN         |
| GET    | `/documents`                  | List accessible documents        | Authenticated |
| GET    | `/documents/{id}`           | Get document by ID               | With READ     |
| DELETE | `/documents/{id}`           | Delete document (soft delete)    | With DELETE   |
| POST   | `/documents/{id}/grant`     | Grant permission to another user | With WRITE    |
| POST   | `/documents/access-check`     | Check access to list of docs     | Authenticated |

---

## 🧪 Postman Collection

- ✅ Import `postman_collection.json` via **Postman → Import → File**
- 🔐 Replace `{adminToken}` / `{userToken}` with tokens from `/auth/login`
- Optionally use a Postman environment for base URL and tokens

---

## 🔧 Setup & Run

### ✅ Local (Without Docker)

1. Open `src/main/resources/application.yml` or `application-dev.yml`
2. Add environment values directly (DB, `jwt.secret`, etc.)
3. Run:

```bash
./mvnw spring-boot:run
```

> 💡 Or pass values inline:

```bash
JWT_SECRET=your-secret ./mvnw spring-boot:run
```

---

### 🐳 Dockerized (Recommended)

1. Create a `.env` file in project root with values:

```env

# PostgreSQL config
POSTGRES_USER=docuser
POSTGRES_PASSWORD=docpass
POSTGRES_DB=docaccess

# Spring Datasource
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/docaccess
SPRING_DATASOURCE_USERNAME=docuser
SPRING_DATASOURCE_PASSWORD=docpass

# JWT Secret
JWT_SECRET=MySuperSecretKey_12345678901234567890
```

2. Run:

```bash
docker-compose up --build
```

**Exposed:**
- App: http://localhost:8080
- DB:  localhost:5432

---

## 🧼 Soft Delete

- Documents are **not permanently removed**
- `@SQLDelete` triggers `UPDATE document SET status='DELETED'`
- All fetch queries use `@Where(status='ACTIVE')`

---

## 📚 Technologies

- Java 17
- Spring Boot 3+
- Spring Security (JWT)
- Spring Data JPA (PostgreSQL)
- Hibernate Soft Delete
- Docker + Docker Compose
- Swagger/OpenAPI
- Lombok

---

## 📅 Last Updated

2025-07-26 22:04:16
