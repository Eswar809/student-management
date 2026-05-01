package com.eswardev.studentmanagement.dao;

import com.eswardev.studentmanagement.entity.AssignmentDetails;

public interface AssignmentDetailsDao {
	
	public AssignmentDetails findByAssignmentAndStudentCourseDetailsId(int assignmentId, int studentCourseDetailsId);
	
	public void save(AssignmentDetails studentCourseAssignmentDetails);
}
