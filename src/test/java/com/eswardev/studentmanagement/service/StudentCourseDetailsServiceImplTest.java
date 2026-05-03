package com.eswardev.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eswardev.studentmanagement.dao.StudentCourseDetailsDao;
import com.eswardev.studentmanagement.entity.StudentCourseDetails;

@ExtendWith(MockitoExtension.class)
public class StudentCourseDetailsServiceImplTest {

    @Mock
    private StudentCourseDetailsDao studentCourseDetailsDao;

    @InjectMocks
    private StudentCourseDetailsServiceImpl studentCourseDetailsService;

    @Test
    public void testFindByStudentAndCourseId() {
        StudentCourseDetails scDetails = new StudentCourseDetails();
        scDetails.setId(1);
        
        when(studentCourseDetailsDao.findByStudentAndCourseId(1, 101)).thenReturn(scDetails);

        StudentCourseDetails result = studentCourseDetailsService.findByStudentAndCourseId(1, 101);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(studentCourseDetailsDao, times(1)).findByStudentAndCourseId(1, 101);
    }

    @Test
    public void testSave() {
        StudentCourseDetails scDetails = new StudentCourseDetails();
        scDetails.setId(1);

        studentCourseDetailsService.save(scDetails);

        verify(studentCourseDetailsDao, times(1)).save(scDetails);
    }
}
