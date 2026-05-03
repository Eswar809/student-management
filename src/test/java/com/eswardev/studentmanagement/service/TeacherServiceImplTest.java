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
import com.eswardev.studentmanagement.dao.TeacherDao;
import com.eswardev.studentmanagement.entity.Role;
import com.eswardev.studentmanagement.entity.Teacher;
import com.eswardev.studentmanagement.user.UserDto;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {

    @Mock
    private TeacherDao teacherDao;

    @Mock
    private RoleDao roleDao;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    public void testLoadUserByUsername_Success() {
        Teacher teacher = new Teacher();
        teacher.setUserName("mr_smith");
        teacher.setPassword("password123");
        Role role = new Role("ROLE_TEACHER");
        teacher.setRole(role);

        when(teacherDao.findByTeacherName("mr_smith")).thenReturn(teacher);

        UserDetails userDetails = teacherService.loadUserByUsername("mr_smith");

        assertNotNull(userDetails);
        assertEquals("mr_smith", userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_TEACHER")));
        verify(teacherDao, times(1)).findByTeacherName("mr_smith");
    }

    @Test
    public void testLoadUserByUsername_Failure() {
        when(teacherDao.findByTeacherName("unknown_user")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            teacherService.loadUserByUsername("unknown_user");
        });

        verify(teacherDao, times(1)).findByTeacherName("unknown_user");
    }

    @Test
    public void testSaveUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUserName("mrs_davis");
        userDto.setPassword("password123");
        userDto.setFirstName("Sarah");
        userDto.setLastName("Davis");
        userDto.setEmail("sarah@example.com");
        Role role = new Role("ROLE_TEACHER");
        userDto.setRole(role);

        teacherService.save(userDto);

        verify(teacherDao, times(1)).save(any(Teacher.class));
    }

    @Test
    public void testFindByTeacherId() {
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setUserName("mr_smith");

        when(teacherDao.findByTeacherId(1)).thenReturn(teacher);

        Teacher foundTeacher = teacherService.findByTeacherId(1);

        assertNotNull(foundTeacher);
        assertEquals(1, foundTeacher.getId());
        verify(teacherDao, times(1)).findByTeacherId(1);
    }
}
