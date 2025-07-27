# 📄 Document Access Control System (Spring Boot + JWT + RBAC)

This is a Document Access Control System built with Java, Spring Boot, and PostgreSQL,
featuring JWT-based authentication, role-based and permission-based access, and soft deletion for documents. 
It includes Swagger API docs, a Dockerized setup (docker-compose), and .env-based configuration for environment variables. 
To run the project, use ./mvnw spring-boot:run locally or docker-compose up --build for a containerized setup.
API testing instructions and the Postman collection are available in the docs/ directory.

**Exposed:**
- App: http://localhost:8080
- DB:  localhost:5432


## 📚 Technologies

- Java 17
- Spring Boot 3+
- Spring Security (JWT)
- Spring Data JPA (PostgreSQL)
- Hibernate Soft Delete
- Docker + Docker Compose
- Swagger/OpenAPI
- Lombok
