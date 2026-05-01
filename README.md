# Student Management System — Role-Based Academic Portal

> **Java · Spring Boot · Spring MVC · Hibernate · MySQL · Maven**

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
| ORM | Hibernate, JPA |
| Database | MySQL 8 |
| Build Tool | Maven |
| Version Control | Git / GitHub |

---

## What Broke (and What I Fixed)

### Bug 1 — Hibernate N+1 Query Problem
**Symptom:** Fetching the course list with enrolled students triggered N+1 SELECT statements — one query for all courses, then one additional query per course to fetch its students. API response time was ~800ms for just 20 records.

**Root cause:** The `@OneToMany` relationship defaulted to `FetchType.LAZY`. When the controller accessed the students collection immediately after fetching courses, Hibernate fired a separate SELECT per course.

**Fix:** Rewrote the repository method using `JOIN FETCH` in JPQL to load the full association in a single query. Response time dropped to ~130ms — approximately **6× improvement**.

```java
// Before: N+1 selects triggered silently
List<Course> courses = courseRepository.findAll();

// After: one JOIN FETCH query
@Query("SELECT c FROM Course c JOIN FETCH c.enrolledStudents")
List<Course> findAllWithStudents();
```

---

### Bug 2 — Spring Security Blocking All API Endpoints
**Symptom:** After adding `spring-boot-starter-security`, every `/api/**` endpoint returned `403 Forbidden`, even public endpoints.

**Root cause:** Spring Security's default configuration secures ALL endpoints. The filter chain ran before requests reached the controller layer.

**Fix:** Configured a `SecurityFilterChain` bean to explicitly permit public routes and require authentication only on admin-scoped routes:

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/public/**").permitAll()
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
    );
    return http.build();
}
```

---

### Bug 3 — Duplicate Enrollment Entries
**Symptom:** A student could enroll in the same course multiple times, creating duplicate grade records.

**Root cause:** Uniqueness was only enforced at the service layer — a check that had a race condition under concurrent requests (two simultaneous enrollments would both pass the check before either committed).

**Fix:** Added a composite unique constraint at the MySQL level on `(student_id, course_id)`. The database now rejects duplicates **atomically**, regardless of concurrency.

```sql
ALTER TABLE enrollment
ADD CONSTRAINT uq_student_course UNIQUE (student_id, course_id);
```

---

## What I'd Do Differently

- Add **Redis caching** for the course catalog (read-heavy, rarely mutated) to reduce DB load under concurrent users
- Implement **pagination** on list endpoints — currently fetches all records, which doesn't scale past ~500 students
- Add **integration tests** with `@SpringBootTest` + H2 in-memory DB to catch the N+1 issue before manual testing

---

## Setup & Run

```bash
git clone https://github.com/Eswar809/student-management.git
cd student-management

# Configure DB connection in src/main/resources/application.properties
# spring.datasource.url=jdbc:mysql://localhost:3306/student_db
# spring.datasource.username=root
# spring.datasource.password=yourpassword

mvn spring-boot:run
# API running at http://localhost:8080
```
