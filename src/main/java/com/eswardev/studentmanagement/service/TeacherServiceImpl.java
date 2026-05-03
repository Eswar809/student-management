package com.eswardev.studentmanagement.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eswardev.studentmanagement.dao.RoleDao;
import com.eswardev.studentmanagement.dao.TeacherDao;
import com.eswardev.studentmanagement.entity.Role;
import com.eswardev.studentmanagement.entity.Teacher;
import com.eswardev.studentmanagement.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TeacherServiceImpl implements TeacherService {
	
	private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

	@Autowired
	private TeacherDao teacherDao;
	
	@Autowired 
	private RoleDao roleDao;
	
	
	@Override
	@Transactional
	public Teacher findByTeacherName(String teacherName) {
		logger.info("Successfully executed findByTeacherName or operation related to it");
		return teacherDao.findByTeacherName(teacherName);
	}

	@Override
	@Transactional
	public void save(UserDto userDto) {
		logger.info("Successfully executed save or operation related to it");
		Teacher teacher = new Teacher();
		teacher.setUserName(userDto.getUserName());
		teacher.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		teacher.setFirstName(userDto.getFirstName());
		teacher.setLastName(userDto.getLastName());
		teacher.setEmail(userDto.getEmail());		
		teacher.setRole(userDto.getRole());	
		
		teacherDao.save(teacher);
	}
	
	@Override
	@Transactional
	public void save(Teacher teacher) {
		logger.info("Successfully executed save or operation related to it");
		teacherDao.save(teacher);	
	}
	
	
	@Override
	@Transactional
	public List<Teacher> findAllTeachers() {
		logger.info("Successfully executed findAllTeachers or operation related to it");
		return teacherDao.findAllTeachers();
	}
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Attempting to load teacher by username: {}", username);
		Teacher teacher = teacherDao.findByTeacherName(username);
		if (teacher == null) {
			logger.error("Teacher not found with username: {}", username);
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		logger.info("Successfully loaded teacher: {}", username);
		Collection<Role> role = new ArrayList<>();
		role.add(teacher.getRole());
		return new org.springframework.security.core.userdetails.User(teacher.getUserName(), teacher.getPassword(),
				mapRolesToAuthorities(role));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public Teacher findByTeacherId(int id) {
		logger.info("Successfully executed findByTeacherId or operation related to it");
		return teacherDao.findByTeacherId(id);
	}

	@Override
	@Transactional
	public void deleteTeacherById(int id) {
		logger.info("Successfully executed deleteTeacherById or operation related to it");
		teacherDao.deleteTeacherById(id);	
	}

	

	

}
