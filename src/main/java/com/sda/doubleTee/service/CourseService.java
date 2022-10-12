package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.AddCourseDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Role;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course findByCodeSection(String code, String section) {
       return courseRepository.findByCodeAndSection(code, section);
    }

    public void saveCourse(AddCourseDto courseDto) {

        Course course = new Course();
        course.setName(courseDto.getName());
        course.setCode(courseDto.getCode());
        course.setCreditHours(courseDto.getCreditHours());
        course.setSection(courseDto.getSection());

        courseRepository.save(course);

    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

}
