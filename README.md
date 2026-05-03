# Student Management System — Role-Based Academic Portal

> **Java 17 · Spring Boot 2.x · Spring Security · Hibernate · MySQL · Docker · JUnit 5 · Mockito · SLF4J**

I built a scalable Java Spring Boot application that serves as a role-based academic portal for Admins, Teachers, and Students. Using the Spring MVC layered architecture, it exposes clean REST APIs backed by a MySQL database via Hibernate. The system allows Admins to manage system-wide academic records, Teachers to post assignments and evaluate submissions, and Students to enroll in courses and track their progress. To ensure the reliability and maintainability of this core logic, I containerized the application with Docker, implemented structured SLF4J observability, and achieved high test coverage (~85%) across the authentication, enrollment, and authorization flows using JUnit 5 and Mockito.

During development, the application's API response time severely degraded because fetching course lists triggered a Hibernate N+1 query problem, firing off separate SELECT statements for every single course's enrolled students. I figured out that this was caused by the `@OneToMany` and `@ManyToMany` relationships eagerly fetching associations or being accessed lazily in loops. I fixed this by strategically configuring `FetchType.LAZY` on the entities and writing a custom JPA `JOIN FETCH` query to load the entire association graph in a single database hit, dropping response times drastically. Additionally, I ran into race conditions allowing students to enroll in the same course multiple times, which I figured out could be robustly solved by moving the uniqueness constraint natively down to the MySQL database level.

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
