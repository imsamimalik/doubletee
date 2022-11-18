package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.RegistrationDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthService authService;

    public Registration findByCourseId(Long id) {
       return registrationRepository.findByCourseId(id).orElse(null);
    }

    public void saveRegistration(RegistrationDto registrationDto) {
        Registration registration  = new Registration();
        Course course = courseRepository.findById(registrationDto.getCourseId()).orElse(null);
        registration.setCourse(course);

        User student = authService.getCurrentUser();
        registration.setStudent(student);

        registrationRepository.save(registration);

    }

    public List<Registration> fetchAll(String email) {
        return registrationRepository.findByStudent_Email(email);
    }

    public void deleteRegistration(Long id) {
        Registration registration =  registrationRepository.findById(id).orElse(null);

        if(registration!=null) {
            String userEmail  = authService.getCurrentUser().getEmail();
            String ownerEmail = registration.getStudent().getEmail();

            if(userEmail==ownerEmail) {
                registrationRepository.deleteById(id);
            }

        }

    }

}
