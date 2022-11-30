package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.RegistrationDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class RegistrationController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/courses/register")
    public String showRegistrationCourses(Model model, Principal principal) {
        List<Course> courses = courseService.findAllCourses();
        String userEmail = principal.getName();
        List<Registration> registrations = registrationService.fetchAllByStudentEmail(userEmail);
        RegistrationDto registrationDto = new RegistrationDto();

        model.addAttribute("courses",courses);
        model.addAttribute("registrationDto",registrationDto);
        model.addAttribute("registrations",registrations);
        return "register-courses";
    }

    @PostMapping("/courses/register")
    public String saveRegistration(@Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto, BindingResult result, Model model, Principal principal) {

        String userEmail = principal.getName();
        User user  = userService.findUserByEmail(userEmail);

        Course course = courseService.findById(registrationDto.getCourseId());

        Registration alreadyRegistered  = registrationService.findByCourseName(course.getName(), user.getId());

        if(alreadyRegistered!=null && alreadyRegistered.getId()!=null) {
            result.rejectValue("courseId", null,
                    "You have already registered this subject.");

            return "redirect:/courses/register?duplicate";

        }


        List<Registration> allRegistrations = registrationService.fetchAllByStudentId(user.getId());

        if(allRegistrations.size()>=7) {
            return "redirect:/courses/register?max";
        }


        if(registrationService.saveRegistration(registrationDto)==false) return "redirect:/courses/register?full";

        return "redirect:/courses/register?success";
    }

    @DeleteMapping("/delete/registration/{id}")
    public String deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return "redirect:/courses/register?success";
    }

    @GetMapping("/my-timetable")
    public String showMyTimeTable(Model model,Principal principal) {

        String userEmail = principal.getName();
        List<Registration> registrations = registrationService.fetchAllByStudentEmail(userEmail);
        Authentication auth = authService.getAuth();
        boolean hasStudentRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_STUDENT"));
        boolean hasFacultyRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_FACULTY"));


        if(hasStudentRole) {
            List<Long> courses = registrations.stream()
                    .map(r->r.getCourse())
                    .map(c->c.getId())
                    .collect(Collectors.toList());

            List<TimeTable> studentTT =  timeTableService.getByCourseIds(courses);

            model.addAttribute("timeTables", studentTT);
            model.addAttribute("title","My TimeTable");
        }
        else if (hasFacultyRole) {

            User user = userService.findUserByEmail(userEmail);
            List<TimeTable> facultyTT = timeTableService.findByTeacherId(user.getEmployeeId());

            model.addAttribute("timeTables", facultyTT);
            model.addAttribute("title","My TimeTable");

        }


        return "timetable";

    }
}
