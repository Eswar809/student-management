package com.eswardev.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.AssignmentDao;
import com.eswardev.studentmanagement.entity.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AssignmentServiceImpl implements AssignmentService {
	private static final Logger logger = LoggerFactory.getLogger(AssignmentServiceImpl.class);

	
	@Autowired
	private AssignmentDao assignmentDao;
	
	@Override
	@Transactional
	public void save(Assignment assignment) {
		logger.info("Successfully executed save or operation related to it");
		assignmentDao.save(assignment);
	}

	@Override
	@Transactional
	public void deleteAssignmentById(int id) {
		logger.info("Successfully executed deleteAssignmentById or operation related to it");
		assignmentDao.deleteAssignmentById(id);
	}

}
