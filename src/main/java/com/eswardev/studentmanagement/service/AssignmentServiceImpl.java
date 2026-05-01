package com.eswardev.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.AssignmentDao;
import com.eswardev.studentmanagement.entity.Assignment;

@Service
public class AssignmentServiceImpl implements AssignmentService {
	
	@Autowired
	private AssignmentDao assignmentDao;
	
	@Override
	@Transactional
	public void save(Assignment assignment) {
		assignmentDao.save(assignment);
	}

	@Override
	@Transactional
	public void deleteAssignmentById(int id) {
		assignmentDao.deleteAssignmentById(id);
	}

}
