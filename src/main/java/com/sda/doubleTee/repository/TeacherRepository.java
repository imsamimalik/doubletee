package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Optional<Teacher> findById(Long id);

}