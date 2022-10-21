package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByEmployeeID(String id);
}