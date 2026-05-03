package com.eswardev.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.StudentCourseDetailsDao;
import com.eswardev.studentmanagement.entity.StudentCourseDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentCourseDetailsServiceImpl implements StudentCourseDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(StudentCourseDetailsServiceImpl.class);

	
	@Autowired
	private StudentCourseDetailsDao studentCourseDetailsDao;
	
	@Override
	@Transactional
	public List<StudentCourseDetails> findByStudentId(int id) {
		logger.info("Successfully executed findByStudentId or operation related to it");
		return studentCourseDetailsDao.findByStudentId(id);
	}

	@Override
	@Transactional
	public StudentCourseDetails findByStudentAndCourseId(int studentId, int courseId) {
		logger.info("Successfully executed findByStudentAndCourseId or operation related to it");
		return studentCourseDetailsDao.findByStudentAndCourseId(studentId, courseId);
	}

	@Override
	@Transactional
	public void deleteByStudentAndCourseId(int studentId, int courseId) {
		logger.info("Successfully executed deleteByStudentAndCourseId or operation related to it");
		studentCourseDetailsDao.deleteByStudentAndCourseId(studentId, courseId);
	}

	@Override
	@Transactional
	public void save(StudentCourseDetails studentCourseDetails) {
		logger.info("Successfully executed save or operation related to it");
		studentCourseDetailsDao.save(studentCourseDetails);
		
	}

	@Override
	@Transactional
	public void deleteByStudentId(int id) {
		logger.info("Successfully executed deleteByStudentId or operation related to it");
		studentCourseDetailsDao.deleteByStudentId(id);
	}

}
