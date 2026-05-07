# Race Condition Fix: Duplicate Enrollment Prevention

## Problem

During load testing, I discovered students could be enrolled in the same course twice. When two concurrent API calls hit `POST /api/enrollments` simultaneously for the same student+course pair, both passed the "already enrolled?" check before either had committed to the database.

**Symptoms:**
- Duplicate rows in `enrollments` table
- Grade calculations returning wrong averages
- Student dashboard showing duplicate course entries

## Root Cause

The enrollment check was done at the application layer:

```java
// BROKEN: check then act — race condition window here
if (enrollmentRepo.existsByStudentAndCourse(student, course)) {
    throw new AlreadyEnrolledException();
}
enrollmentRepo.save(new Enrollment(student, course)); // another thread can save here too
```

Two threads could both pass the `existsBy` check before either saved.

## Fix

### Step 1: Database-level unique constraint (primary defense)

```sql
ALTER TABLE enrollments
ADD CONSTRAINT uq_student_course UNIQUE (student_id, course_id);
```

In Spring Boot migration / entity:
```java
@Table(
    uniqueConstraints = @UniqueConstraint(
        name = "uq_student_course",
        columnNames = {"student_id", "course_id"}
    )
)
```

### Step 2: Catch the constraint violation gracefully

```java
try {
    enrollmentRepo.save(new Enrollment(student, course));
} catch (DataIntegrityViolationException e) {
    throw new AlreadyEnrolledException("Student already enrolled in this course");
}
```

## Result

Duplicate enrollments now **impossible** — even under concurrent load. The database constraint is the last line of defense regardless of application-level bugs.

## Lesson

Never rely solely on application-level checks for uniqueness. Always enforce critical data integrity constraints at the **database level**.
