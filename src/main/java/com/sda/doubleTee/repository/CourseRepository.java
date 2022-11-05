package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCodeAndSection(String courseCode, String section);
}