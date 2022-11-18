package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByCourseId(Long id);

    List<Registration> findByStudent_Id(Long id);

    List<Registration> findByStudent_Email(String email);


}