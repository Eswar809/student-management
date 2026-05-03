# Student Management System — Role-Based Academic Portal

> **Java 17 · Spring Boot 2.x · Spring Security · Hibernate · MySQL · Docker · JUnit 5 · Mockito · SLF4J**

A scalable, backend-focused web application managing academic workflows across three distinct user roles — Admin, Student, and Teacher. Built with a clean REST API separation using the Spring MVC layered architecture, it demonstrates enterprise-grade practices including proper ORM mappings, comprehensive testing, containerization, and observability.

---

## 🚀 Key Features

* **Role-Based Access Control (RBAC):** Secure authentication and authorization using Spring Security.
  * **Admins:** Manage users, courses, and system-wide academic records.
  * **Teachers:** Create courses, post assignments, and evaluate student submissions.
  * **Students:** Enroll in courses, track assignments, and monitor academic progress.
* **Optimized Database Queries:** Eliminated N+1 query problems using JPA `JOIN FETCH` and strategic `FetchType.LAZY` configurations.
* **High Reliability:** Core business logic (Enrollment, Authentication, Authorization) is heavily tested (~85% line coverage) using **JUnit 5** and **Mockito**.
* **Observability:** Centralized, structured logging implemented across all layers using **SLF4J** and **Logback**.
* **DevOps Ready:** Fully containerized environment with a multi-container **Docker Compose** setup for seamless local development.

---

## 🛠️ Tech Stack

| Component | Technology |
|---|---|
| **Core** | Java 17 |
| **Framework** | Spring Boot 2.6.x, Spring MVC |
| **Security** | Spring Security |
| **Data Access** | Hibernate, Spring Data JPA |
| **Database** | MySQL 8 |
| **Build Tool** | Maven |
| **Testing** | JUnit 5, Mockito |
| **Observability** | SLF4J, Logback |
| **DevOps** | Docker, Docker Compose |

---

## 🧠 Architectural Enhancements & Problem Solving

### 1. Eradicating the Hibernate N+1 Query Problem
**Symptom:** Fetching the course list with enrolled students triggered N+1 SELECT statements — one query for all courses, then one additional query per course to fetch its students. API response time was severely degraded.
**Root cause:** The `@OneToMany` and `@ManyToMany` relationships defaulted to `FetchType.EAGER` or were accessed lazily in loops causing multiple database hits.
**Fix:** Refactored entity associations to `FetchType.LAZY` and rewrote the DAO layer using `JOIN FETCH` to load the full association graph in a single, optimized query.

```java
// Optimized single-query fetch
List<Course> courses = session.createQuery(
    "select distinct c from Course c left join fetch c.students", 
    Course.class
).getResultList();
```

### 2. Comprehensive Testing Strategy
Added robust **JUnit 5** and **Mockito** unit tests for the core service layers (`StudentServiceImpl`, `TeacherServiceImpl`, and `StudentCourseDetailsServiceImpl`). The test suite rigorously validates the enrollment flow, authentication logic (`loadUserByUsername`), and role-authorization rules, ensuring deterministic behavior and preventing regressions.

### 3. Enterprise-Grade Observability
Transitioned the application to use **SLF4J with Logback** for centralized, structured logging.
* Successful system events (e.g., enrollments, authentication mappings) utilize `INFO` logging.
* Exceptions (e.g., `UsernameNotFoundException`) are appropriately trapped and routed to `ERROR` logs.
* Configured `logback-spring.xml` to stream logs to both the console and persistent file storage (`logs/application.log`).

### 4. Preventing Duplicate Enrollment Entries
**Symptom:** Race conditions allowed a student to enroll in the same course multiple times.
**Fix:** Enforced data integrity at the database level by adding a composite unique constraint on `(student_id, course_id)`.

---

## 🐳 Setup & Run

### Using Docker (Recommended)
The fastest way to run the application and its dependencies.

```bash
git clone https://github.com/Eswar809/student-management.git
cd student-management

# Spin up both MySQL and the Spring Boot application containers
docker-compose up --build
```
*The application will be available at `http://localhost:8080`.*

### Using Maven (Local Development)
Ensure you have a local MySQL server running and configured.

1. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_security_custom_user_demo?useSSL=false&serverTimezone=UTC
spring.datasource.username=springstudent
spring.datasource.password=springstudent
```

2. Build and run the application:
```bash
mvn clean install
mvn spring-boot:run
```
