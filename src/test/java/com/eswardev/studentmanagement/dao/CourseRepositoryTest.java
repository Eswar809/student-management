package com.eswardev.studentmanagement.dao;

import com.eswardev.studentmanagement.entity.Course;
import com.eswardev.studentmanagement.entity.Student;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Test
    @Transactional
    public void findAllActiveCoursesWithStudents_avoids_n_plus_1() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        
        // Enable statistics
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        Statistics stats = sessionFactory.getStatistics();
        stats.clear();

        List<Course> courses = courseRepository.findAllActiveCoursesWithStudents();
        courses.forEach(c -> c.getStudents().size()); // force access
        
        // Expected is 1 query for fetching courses and their students
        assertThat(stats.getPrepareStatementCount()).isEqualTo(1L);
    }
}

