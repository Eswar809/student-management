# Student Management System

**What I built**
I built a scalable Java Spring Boot application acting as a role-based academic portal for Admins, Teachers, and Students, exposing clean REST APIs backed by a MySQL database via Hibernate. I containerized the application with Docker, implemented structured SLF4J observability, and achieved ~85% test coverage across authentication, enrollment, and authorization flows using JUnit 5 and Mockito. The system also features a dynamic, server-side-optimized CSV export for admins by streaming `HttpServletResponse` payloads directly via `PrintWriter`, bypassing inefficient local server disk I/O.

**What broke and what I figured out**
During development, the application's API response time severely degraded because fetching course lists triggered a Hibernate N+1 query problem, firing separate SELECT statements for every single course's enrolled students. I realized this was caused by `@OneToMany` and `@ManyToMany` relationships eagerly fetching associations or being accessed lazily in loops. I fixed this by strategically configuring `FetchType.LAZY` on the entities and writing a custom JPA `JOIN FETCH` query to load the entire association graph in a single database hit, dropping response times drastically. I also solved race conditions allowing students to enroll in the same course multiple times by moving the uniqueness constraint down to the MySQL database level natively.

## Setup & Run

### Using Docker (Recommended)
```bash
# Spin up both MySQL and the Spring Boot application containers
docker-compose up --build
```
*The application will be available at `http://localhost:8080`.*

### Using Maven (Local Development)
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