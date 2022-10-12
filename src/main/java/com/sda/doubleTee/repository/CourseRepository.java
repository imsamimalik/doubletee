package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCodeAndSection(String courseCode, String section);
}