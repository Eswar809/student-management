package com.eswardev.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.eswardev.studentmanagement.dao.RoleDao;
import com.eswardev.studentmanagement.dao.StudentDao;
import com.eswardev.studentmanagement.entity.Role;
import com.eswardev.studentmanagement.entity.Student;
import com.eswardev.studentmanagement.user.UserDto;

@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    public void testLoadUserByUsername_Success() {
        Student student = new Student();
        student.setUserName("john_doe");
        student.setPassword("password123");
        Role role = new Role("ROLE_STUDENT");
        student.setRole(role);

        when(studentDao.findByStudentName("john_doe")).thenReturn(student);

        UserDetails userDetails = studentService.loadUserByUsername("john_doe");

        assertNotNull(userDetails);
        assertEquals("john_doe", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT")));
        verify(studentDao, times(1)).findByStudentName("john_doe");
    }

    @Test
    public void testLoadUserByUsername_Failure() {
        when(studentDao.findByStudentName("unknown_user")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            studentService.loadUserByUsername("unknown_user");
        });

        verify(studentDao, times(1)).findByStudentName("unknown_user");
    }

    @Test
    public void testSaveUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUserName("jane_doe");
        userDto.setPassword("password123");
        userDto.setFirstName("Jane");
        userDto.setLastName("Doe");
        userDto.setEmail("jane@example.com");
        Role role = new Role("ROLE_STUDENT");
        userDto.setRole(role);

        studentService.save(userDto);

        verify(studentDao, times(1)).save(any(Student.class));
    }

    @Test
    public void testFindByStudentId() {
        Student student = new Student();
        student.setId(1);
        student.setUserName("john_doe");

        when(studentDao.findByStudentId(1)).thenReturn(student);

        Student foundStudent = studentService.findByStudentId(1);

        assertNotNull(foundStudent);
        assertEquals(1, foundStudent.getId());
        verify(studentDao, times(1)).findByStudentId(1);
    }
}
