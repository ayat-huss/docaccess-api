
Document Access Control System (Spring Boot + JWT + RBAC)

A secure and containerized Spring Boot REST API for managing documents with JWT Authentication, Role-Based Access Control, and fine-grained permissions.

** Key Features
1- JWT Auth – Login & receive a secure token

2- RBAC – Admin and User roles

3- Permissions – Grant READ / WRITE / DELETE access

4- Document CRUD – Admin creates/deletes; users read/edit based on permissions

5- Batch Access Check – Check access for multiple documents at once

6- Soft Delete – Documents are not removed from DB, only marked deleted

7- Postman Collection – Available in docs/ folder

8- Swagger UI – Interactive API documentation

* Tech Stack
-Java 17, Spring Boot 3+, Spring Security (JWT), Spring Data JPA (PostgreSQL)

-Hibernate Soft Delete, Lombok

-Docker + Docker Compose

-Swagger / OpenAPI for API docs

*Environment
-App: http://localhost:8080

-DB: localhost:5432 (PostgreSQL)