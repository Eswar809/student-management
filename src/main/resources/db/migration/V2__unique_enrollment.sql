-- Add unique constraint on (student_id, course_id)
ALTER TABLE enrollments ADD CONSTRAINT unique_enrollment UNIQUE (student_id, course_id);
