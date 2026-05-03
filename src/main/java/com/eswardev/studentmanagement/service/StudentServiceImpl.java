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
import com.eswardev.studentmanagement.dao.StudentDao;
import com.eswardev.studentmanagement.entity.Role;
import com.eswardev.studentmanagement.entity.Student;
import com.eswardev.studentmanagement.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StudentServiceImpl implements StudentService {
	
	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired 
	private RoleDao roleDao;
	

	@Override
	@Transactional
	public Student findByStudentName(String studentName) {
		logger.info("Successfully executed findByStudentName or operation related to it");
		return studentDao.findByStudentName(studentName);
	}
	
	@Override
	@Transactional
	public Student findByStudentId(int id) {
		logger.info("Successfully executed findByStudentId or operation related to it");
		return studentDao.findByStudentId(id);
	}

	@Override
	@Transactional
	public void save(UserDto userDto) {
		logger.info("Successfully executed save or operation related to it");
		Student student = new Student();
		student.setUserName(userDto.getUserName());
		student.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		student.setFirstName(userDto.getFirstName());
		student.setLastName(userDto.getLastName());
		student.setEmail(userDto.getEmail());		
		student.setRole(userDto.getRole());	
		
		studentDao.save(student);
	}
	
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Attempting to load student by username: {}", username);
		Student student = studentDao.findByStudentName(username);
		if (student == null) {
			logger.error("Student not found with username: {}", username);
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		logger.info("Successfully loaded student: {}", username);
		Collection<Role> role = new ArrayList<>();
		role.add(student.getRole());
		return new org.springframework.security.core.userdetails.User(student.getUserName(), student.getPassword(),
				mapRolesToAuthorities(role));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<Student> findAllStudents() {
		logger.info("Successfully executed findAllStudents or operation related to it");
		return studentDao.findAllStudents();
	}

	@Override
	@Transactional
	public void save(Student student) {
		logger.info("Successfully executed save or operation related to it");
		studentDao.save(student);
		
	}

	@Override
	@Transactional
	public void deleteById(int id) {
		logger.info("Successfully executed deleteById or operation related to it");
		studentDao.deleteById(id);
	}

}
