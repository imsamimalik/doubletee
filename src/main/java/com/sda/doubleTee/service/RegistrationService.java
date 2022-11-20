package com.sda.doubleTee.service;

import com.sda.doubleTee.dao.TimeSlot;
import com.sda.doubleTee.dto.RegistrationDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RegistrationRepository;
import com.sda.doubleTee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    @Autowired
    private TimeTableService timeTableService;

    public Registration findByCourseId(Long course, Long student) {
       return registrationRepository.findByCourse_IdAndStudent_Id(course, student).orElse(null);
    }

    public boolean saveRegistration(RegistrationDto registrationDto) {
        Registration registration  = new Registration();

        Course course = courseRepository.findById(registrationDto.getCourseId()).orElse(null);

        if(course.getCapacity()<=0) return  false;

            course.setCapacity(course.getCapacity()-1);
            courseRepository.save(course);

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

    public List<Registration> fetchAllByStudentEmail(String email) {
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

    public List<TimeSlot> getStudentAvailability(User student, String day) {
        List<Registration> registrations =  registrationRepository.findByStudent_Id(student.getId());

        List<Long> courses = registrations.stream().map(r -> r.getCourse()).map(c->c.getId()).toList();

        List<TimeTable> allocations = timeTableService.getByCourseIds(courses);


        List<TimeSlot> slots = new ArrayList<>(allocations.stream().map(t -> {
            return new TimeSlot(t.getStartTime(), t.getEndTime());
        }).toList());

        slots.sort(Comparator.comparing(TimeSlot::getStartTime));

        List<TimeSlot> freeSlots = new ArrayList<TimeSlot>();
        LocalTime start = LocalTime.of(8,0,0);
        LocalTime end = LocalTime.of(21,0,0);

        if(slots.size()==0) {
            freeSlots.add(new TimeSlot(start,end));
            return freeSlots;
        }

        for (int i = 0; i<slots.size(); i++) {
            TimeSlot slot  = slots.get(i);

            if(i==0 && !start.equals(slot)) {
                freeSlots.add(new TimeSlot(start,slot.getStartTime()));
            }
            else if(!slots.get(i-1).getEndTime().equals(slot.getStartTime())){
                freeSlots.add(new TimeSlot(slots.get(i-1).getEndTime(), slot.getStartTime()));
            }
        }


        freeSlots.add(new TimeSlot(slots.get(slots.size()-1).getEndTime(),end));

        return freeSlots;

    }


}
