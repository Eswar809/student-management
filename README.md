# Student Management System — Role-Based Academic Portal

> **Java · Spring Boot · Spring MVC · Hibernate · MySQL · Docker · JUnit 5**

A backend-focused web application managing academic workflows across three user roles — Admin, Student, and Teacher — with clean REST API separation using Spring MVC layered architecture.

---

## What I Built

A full-stack Java Spring Boot application with **role-based access control (RBAC)**:
- **Admins** manage users, courses, and academic records
- **Teachers** post assignments and submit grades
- **Students** enroll in courses and track their academic progress

All operations are exposed as **CRUD REST APIs** with proper service-repository separation and Hibernate ORM for database mapping.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring MVC |
| Security | Spring Security |
| ORM | Hibernate, JPA |
| Database | MySQL 8 |
| Build Tool | Maven |
| Testing | JUnit 5, Mockito |
| Observability | SLF4J, Logback |
| DevOps | Docker, Docker Compose |

---

## What Broke (and What I Fixed)

### Bug 1 — Hibernate N+1 Query Problem
**Symptom:** Fetching the course list with enrolled students triggered N+1 SELECT statements — one query for all courses, then one additional query per course to fetch its students. API response time was ~800ms for just 20 records.

**Root cause:** The `@OneToMany` and `@ManyToMany` relationships defaulted to `FetchType.EAGER` or were accessed lazily in loops causing multiple fetches.

**Fix:** Changed associations to `FetchType.LAZY` and rewrote the DAO methods using `JOIN FETCH` to load the full association in a single query. Response time dropped significantly.

```java
// After: one JOIN FETCH query
List<Course> courses = session.createQuery("select distinct c from Course c left join fetch c.students", Course.class).getResultList();
```

### Bug 2 — Spring Security Blocking All API Endpoints
**Symptom:** After adding `spring-boot-starter-security`, endpoints returned `403 Forbidden` or `401 Unauthorized`.
**Fix:** Configured `SecurityFilterChain` / Security Config classes to correctly permit public routes and implement RBAC.

### Bug 3 — Duplicate Enrollment Entries
**Symptom:** A student could enroll in the same course multiple times.
**Fix:** Added a composite unique constraint at the MySQL level on `(student_id, course_id)`.

---

## Recent Architectural Enhancements

- **Comprehensive Testing Strategy:** Added robust JUnit 5 and Mockito unit tests for the core service layers (`StudentServiceImpl`, `TeacherServiceImpl`, and `StudentCourseDetailsServiceImpl`), particularly ensuring ~85% line coverage for the enrollment flow, authentication logic (`loadUserByUsername`), and role-authorization rules.
- **Enterprise-Grade Observability:** Transitioned to SLF4J with Logback for centralized logging across all Controllers and Services. Successful system events (e.g., enrollments, logins) use INFO logging, and exceptions (like `UsernameNotFoundException`) are appropriately trapped and routed to ERROR logs. The logs are both console and file-bound.
- **DevOps Readiness:** Containerized the Spring Boot application using a streamlined OpenJDK 17 base image `Dockerfile` and configured `docker-compose.yml` to orchestrate both the application and the MySQL 8 database service together for seamless deployment.

---

## Setup & Run

### Using Docker (Recommended)
```bash
git clone https://github.com/Eswar809/student-management.git
cd student-management

# Spin up both MySQL and the Spring Boot application
docker-compose up --build
```

### Using Maven (Local)
```bash
# Ensure local MySQL server is running and configured per application.properties
mvn clean install
mvn spring-boot:run
# API running at http://localhost:8080
```
