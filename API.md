# REST API Documentation

## Base URL
`http://localhost:8080/api`

## Authentication
All endpoints require JWT token in header:
```
Authorization: Bearer <token>
```

---

## Auth Endpoints

### POST /api/auth/login
Login and receive JWT token.

**Request:**
```json
{
  "username": "admin@college.edu",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "role": "ADMIN",
  "expiresIn": 86400
}
```

---

## Student Endpoints

### GET /api/students
Get all students (Admin/Teacher only).

**Response:**
```json
[
  {
    "id": 1,
    "name": "Ravi Kumar",
    "email": "ravi@college.edu",
    "enrolledCourses": ["CS101", "MATH201"]
  }
]
```

### GET /api/students/{id}
Get student by ID.

### POST /api/students
Create new student (Admin only).

**Request:**
```json
{
  "name": "Priya Sharma",
  "email": "priya@college.edu",
  "role": "STUDENT"
}
```

---

## Course Endpoints

### GET /api/courses
Get all courses with enrolled student count.

### GET /api/courses/{id}
Get course with full student list (uses JOIN FETCH — see DEBUGGING.md).

### POST /api/courses
Create course (Admin only).

---

## Enrollment Endpoints

### POST /api/enrollments
Enroll student in course.

**Request:**
```json
{
  "studentId": 1,
  "courseId": 3
}
```

**Error Responses:**
- `409 Conflict` — Student already enrolled (unique constraint enforced at DB level)
- `404 Not Found` — Student or course doesn't exist
- `403 Forbidden` — Course at max capacity

### DELETE /api/enrollments/{id}
Unenroll student from course (Admin only).

---

## Error Response Format
All errors return:
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Student already enrolled in this course",
  "timestamp": "2026-05-07T06:45:00Z"
}
```
