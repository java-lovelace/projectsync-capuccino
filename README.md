# ProjectSync

ProjectSync is a simple project management/tracking application built with **Java 21**, **Spring Boot**, **Spring Data JPA (Hibernate)** and **MySQL 8**. The frontend is a minimal **Bootstrap** application that interacts with the backend REST API.

## Features implemented
- Full CRUD for Projects (name, status, description, owner)
- Validation for required fields
- Auditing fields (createdAt, updatedAt)
- Simple Bootstrap-based frontend to manage projects
- JPA/Hibernate as ORM

## Tech stack
- Java 21
- Spring Boot (Web, Data JPA, Validation)
- MySQL 8 (configured via application.properties)
- Bootstrap 5 (frontend)
- JUnit 5 (tests)

## Setup and run
1. Install Java 21 and Maven.
2. Create a MySQL database and user, for example:
   ```sql
   CREATE DATABASE projectsync;
   CREATE USER 'project'@'localhost' IDENTIFIED BY 'changeme';
   GRANT ALL PRIVILEGES ON projectsync.* TO 'project'@'localhost';
   FLUSH PRIVILEGES;
   ```
3. Update `src/main/resources/application.properties` with your DB credentials.
4. Build and run:
   ```bash
   mvn -U clean package
   mvn spring-boot:run
   ```
5. Open `http://localhost:8080` in your browser.

## Azure DevOps traceability (guidance)
See `AZURE_DEVOPS_TRACEABILITY.md` for a template describing Epics, Features, User Stories and how to link commits/branches/PRs to Work Items.

## Tests
Run unit and integration tests with:
```bash
mvn test
```

## Notes
This project is scaffolded to be simple and educational. For production use, add authentication, pagination, DTO mapping libraries, and CI/CD pipelines.
