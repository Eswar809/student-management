# N+1 Query Debugging Runbook

## Problem

When listing all courses with enrolled students, the API response time was severely degraded (~800ms for small datasets). Using Hibernate's query logging, I observed the following pattern:

```
Hibernate: select c.* from courses c
Hibernate: select s.* from students s where s.course_id = 1
Hibernate: select s.* from students s where s.course_id = 2
Hibernate: select s.* from students s where s.course_id = 3
... (one SELECT per course)
```

For 50 courses, this fired **51 SQL queries** instead of 1.

## Root Cause

The `Course` entity had `@OneToMany(fetch = FetchType.EAGER)` on the students collection. Hibernate was loading every student list lazily-turned-eager — one query per course row.

## Fix Applied

### Before (bad):
```java
@OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
private List<Student> students;
```

### After (fixed):
```java
@OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
private List<Student> students;
```

And in the repository, a custom JPQL JOIN FETCH query:
```java
@Query("SELECT c FROM Course c JOIN FETCH c.students WHERE c.id = :id")
Optional<Course> findByIdWithStudents(@Param("id") Long id);
```

## Result

SQL query count dropped from **N+1 (51 queries)** → **1 JOIN query**.
Response time for `/api/courses` improved from ~800ms → ~120ms on the same dataset.

## Lesson

Always check Hibernate query logs (`spring.jpa.show-sql=true`) during development. N+1 is invisible at small scale but destroys performance in production.
