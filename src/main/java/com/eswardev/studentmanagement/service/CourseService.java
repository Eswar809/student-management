package com.eswardev.studentmanagement.service;

import java.util.List;

import com.eswardev.studentmanagement.entity.Course;

public interface CourseService {
	
	public void save(Course course);
	
	public List<Course> findAllCourses();
	
	public Course findCourseById(int id);
	
	public void deleteCourseById(int id);
}
