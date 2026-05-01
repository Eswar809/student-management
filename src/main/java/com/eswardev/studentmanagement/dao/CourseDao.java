package com.eswardev.studentmanagement.dao;

import java.util.List;

import com.eswardev.studentmanagement.entity.Course;

public interface CourseDao {
	
	public void saveCourse(Course course);
	
	public List<Course> findAllCourses();
	
	public Course findCourseById(int id);
	
	public void deleteCourseById(int id);
}
