package com.sda.doubleTee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sda.doubleTee.model.Registration;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByCourse_IdAndStudent_Id(Long course, Long student);

    List<Registration> findByStudent_Id(Long id);

    List<Registration> findByStudent_Email(String email);

    Optional<Registration> findByCourse_NameAndStudentId(String course, Long student);

}