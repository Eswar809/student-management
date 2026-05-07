package com.eswardev.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.CourseDao;
import com.eswardev.studentmanagement.dao.CourseRepository;
import com.eswardev.studentmanagement.entity.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CourseServiceImpl implements CourseService {
	private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

	
	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CourseRepository courseRepository;
	
	@Override
	@Transactional
	public void save(Course course) {
		logger.info("Successfully executed save or operation related to it");
		courseDao.saveCourse(course);
	}

	@Override
	@Transactional
	public List<Course> findAllCourses() {
		logger.info("Successfully executed findAllCourses or operation related to it");
		return courseRepository.findAllActiveCoursesWithStudents();
	}

	@Override
	@Transactional
	public Course findCourseById(int id) {
		logger.info("Successfully executed findCourseById or operation related to it");
		return courseDao.findCourseById(id);
	}

	@Override
	@Transactional
	public void deleteCourseById(int id) {
		logger.info("Successfully executed deleteCourseById or operation related to it");
		courseDao.deleteCourseById(id);		
	}

}
