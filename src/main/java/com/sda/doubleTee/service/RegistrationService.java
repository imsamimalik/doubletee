package com.sda.doubleTee.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sda.doubleTee.dto.TimeSlot;
import com.sda.doubleTee.dto.RegistrationDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RegistrationRepository;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AuthService authService;


    @Autowired
    private TimeTableService timeTableService;

    public Registration findByCourseId(Long course, Long student) {
       return registrationRepository.findByCourse_IdAndStudent_Id(course, student).orElse(null);
    }

    public Registration findByCourseName(String course, Long student) {
        return registrationRepository.findByCourse_NameAndStudentId(course, student).orElse(null);
    }

    public boolean saveRegistration(RegistrationDto registrationDto) {
        Registration registration  = new Registration();

        Course course = courseRepository.findById(registrationDto.getCourseId()).orElse(null);

        if(course.getSeats()<=0) return  false;

            course.setSeats(course.getSeats()-1);
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

    public List<Registration> fetchAllByStudentId(Long id) {
        return registrationRepository.findByStudent_Id(id);
    }

    public List<Registration> fetchAllByStudentEmail(String email) {
        return registrationRepository.findByStudent_Email(email);
    }

    public void deleteRegistration(Long id) {
        Registration registration =  registrationRepository.findById(id).orElse(null);

        Course course = courseRepository.findById(registration.getCourse().getId()).orElse(null);

        if(registration!=null) {
                registrationRepository.deleteById(id);
                course.setSeats(course.getSeats()+1);
                courseRepository.save(course);

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
                if(MINUTES.between(start,slot.getStartTime())>10) {
                    freeSlots.add(new TimeSlot(start,slot.getStartTime()));
                }
            }
            else if(!slots.get(i-1).getEndTime().equals(slot.getStartTime())){
                if(MINUTES.between(slots.get(i-1).getEndTime(),slot.getStartTime())>10) {
                    freeSlots.add(new TimeSlot(slots.get(i-1).getEndTime(), slot.getStartTime()));
                }
            }
        }


        freeSlots.add(new TimeSlot(slots.get(slots.size()-1).getEndTime(),end));

        return freeSlots;

    }


}
