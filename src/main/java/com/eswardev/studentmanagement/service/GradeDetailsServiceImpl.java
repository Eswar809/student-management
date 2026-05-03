package com.eswardev.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.GradeDetailsDao;
import com.eswardev.studentmanagement.entity.GradeDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GradeDetailsServiceImpl implements GradeDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(GradeDetailsServiceImpl.class);


	@Autowired
	private GradeDetailsDao gradeDetailsDao;
	
	@Override
	@Transactional
	public void save(GradeDetails gradeDetails) {
		logger.info("Successfully executed save or operation related to it");
		gradeDetailsDao.save(gradeDetails);
	}

	@Override
	@Transactional
	public GradeDetails findById(int id) {
		logger.info("Successfully executed findById or operation related to it");
		return gradeDetailsDao.findById(id);
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		logger.info("Successfully executed deleteById or operation related to it");
		gradeDetailsDao.deleteById(id);
	}

}
