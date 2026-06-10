# Student Management System — Role-Based Academic Portal

A Spring Boot (Java 17) academic portal with three authenticated roles — **Admin**, **Teacher**, **Student** — covering course management, enrollment, assignments, and grading, backed by MySQL via Hibernate/JPA and shipped with Docker.

Maintained by [Deevi Eswar](https://github.com/Eswar809).

---

## ⚙️ Engineering highlights

**1. Race condition: concurrent duplicate enrollment — fixed at the database level.**
Load testing surfaced a classic check-then-act race: two simultaneous `POST /api/enrollments` calls for the same student+course both passed the "already enrolled?" check before either committed. Fixed with a DB-level unique constraint (`uq_student_course`) as the primary defense, with the `DataIntegrityViolationException` caught and translated into a clean API error. Verified by a dedicated concurrency test.
→ Full write-up: [`RACE_CONDITION_FIX.md`](RACE_CONDITION_FIX.md) · Migration: [`V2__unique_enrollment.sql`](src/main/resources/db/migration/V2__unique_enrollment.sql) · Test: [`EnrollmentConcurrencyTest`](src/test/java/com/studentmgmt/EnrollmentConcurrencyTest.java)

**2. N+1 query problem: course listings fired one SELECT per course.**
Fetching course lists triggered Hibernate's N+1 pattern through `@OneToMany`/`@ManyToMany` associations. Fixed with `FetchType.LAZY` plus a custom JPA `JOIN FETCH` query that loads the association graph in a single database hit — with a regression test so it can't silently come back.
→ Debugging runbook with before/after SQL traces: [`DEBUGGING.md`](DEBUGGING.md)

**3. Role-based security.**
Spring Security with three roles, a custom authentication success handler that routes each role to its own landing page, and access-denied handling. Authentication failure paths are covered by tests.

**4. Operational basics.**
Dockerfile + docker-compose with a MySQL **health check** (app waits for the DB), SLF4J/Logback structured logging config, and streaming CSV export for admins (written directly to `HttpServletResponse` — no temp files on disk).

---

## ✅ Tests

JUnit 5 + Mockito suite targeting the risky paths, not just happy paths:

| Test | What it proves |
|---|---|
| `EnrollmentConcurrencyTest` | Duplicate enrollment impossible under concurrent load |
| `EnrollmentBoundaryTest` | Max-capacity and closed-course edge cases rejected |
| `AuthFailureTest` | Bad credentials / unauthorized access handled correctly |
| `CourseRepositoryTest` | JOIN FETCH query returns the full graph in one hit (N+1 regression guard) |
| `StudentServiceImplTest`, `TeacherServiceImplTest`, `StudentCourseDetailsServiceImplTest` | Service-layer behavior with mocked DAOs |

```bash
mvn test
```

---

## 👥 Features by role

| Role | Capabilities |
|---|---|
| **Admin** | Manage students, teachers, and courses; view enrollments; CSV export |
| **Teacher** | Manage own courses, create assignments, track submission status, edit grades |
| **Student** | Browse and enroll in courses, view assignments, submit work, see grades |

REST API reference with request/response examples: [`API.md`](API.md)

---

## 🚀 Run it

### Docker (recommended)
```bash
docker-compose up --build
# app: http://localhost:8080  (MySQL starts first via health check)
```
Troubleshooting: [`DOCKER_TROUBLESHOOTING.md`](DOCKER_TROUBLESHOOTING.md)

### Maven (local)
1. Set your MySQL credentials in `src/main/resources/application.properties`
2. ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---

## 🧱 Tech stack

Java 17 · Spring Boot · Spring Security · Spring Data JPA / Hibernate · MySQL · Thymeleaf · JUnit 5 + Mockito · Docker / docker-compose · SLF4J + Logback

## 📚 Docs

[`API.md`](API.md) · [`RACE_CONDITION_FIX.md`](RACE_CONDITION_FIX.md) · [`DEBUGGING.md`](DEBUGGING.md) · [`DOCKER_TROUBLESHOOTING.md`](DOCKER_TROUBLESHOOTING.md) · [`CODE_REVIEWS.md`](CODE_REVIEWS.md)
