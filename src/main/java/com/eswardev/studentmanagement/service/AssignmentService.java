package com.eswardev.studentmanagement.service;

import com.eswardev.studentmanagement.entity.Assignment;

public interface AssignmentService {
	
	public void save(Assignment assignment);
	
	public void deleteAssignmentById(int id);
}
