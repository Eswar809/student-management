package com.eswardev.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.AssignmentDetailsDao;
import com.eswardev.studentmanagement.entity.AssignmentDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssignmentDetailsServiceImpl implements AssignmentDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(AssignmentDetailsServiceImpl.class);

	
	@Autowired
	private AssignmentDetailsDao studentCourseAssignmentDetailsDao;
	
	@Override
	@Transactional
	public AssignmentDetails findByAssignmentAndStudentCourseDetailsId(int assignmentId,
			int studentCourseDetailsId) {
		logger.info("Successfully executed findByAssignmentAndStudentCourseDetailsId or operation related to it");
		return studentCourseAssignmentDetailsDao.findByAssignmentAndStudentCourseDetailsId(assignmentId, studentCourseDetailsId);
		 
	}

	@Override
	@Transactional
	public void save(AssignmentDetails studentCourseAssignmentDetails) {
		logger.info("Successfully executed save or operation related to it");
		studentCourseAssignmentDetailsDao.save(studentCourseAssignmentDetails);
	}

}
