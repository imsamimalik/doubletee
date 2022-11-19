package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.RegistrationDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RegistrationRepository;
import com.sda.doubleTee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    public Registration findByCourseId(Long course, Long student) {
       return registrationRepository.findByCourse_IdAndStudent_Id(course, student).orElse(null);
    }

    public boolean saveRegistration(RegistrationDto registrationDto) {
        Registration registration  = new Registration();

        Authentication auth = authService.getAuth();

        boolean hasStudentRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));

        Course course = courseRepository.findById(registrationDto.getCourseId()).orElse(null);

        if(course.getCapacity()<=0) return  false;

        if(hasStudentRole==true) {
            course.setCapacity(course.getCapacity()-1);
            courseRepository.save(course);
        }

        registration.setCourse(course);

        User student = authService.getCurrentUser();
        registration.setStudent(student);

        registrationRepository.save(registration);
        return true;

    }

    public List<Registration> fetchAll() {
        return registrationRepository.findAll();
    }

    public List<Registration> fetchAllById(Long id) {
        return registrationRepository.findByStudent_Id(id);
    }

    public List<Registration> fetchAllByEmail(String email) {
        return registrationRepository.findByStudent_Email(email);
    }

    public void deleteRegistration(Long id) {
        Registration registration =  registrationRepository.findById(id).orElse(null);

        Authentication auth = authService.getAuth();

        boolean hasStudentRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));

        Course course = courseRepository.findById(registration.getCourse().getId()).orElse(null);


        if(registration!=null) {
            String userEmail  = authService.getCurrentUser().getEmail();
            String ownerEmail = registration.getStudent().getEmail();

            if(userEmail==ownerEmail) {
                registrationRepository.deleteById(id);

                if(hasStudentRole==true) {
                    course.setCapacity(course.getCapacity()+1);
                    courseRepository.save(course);
                }

            }

        }

    }

}
