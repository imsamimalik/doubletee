package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.RegistrationDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.service.AuthService;
import com.sda.doubleTee.service.CourseService;
import com.sda.doubleTee.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class RegistrationController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private AuthService authService;


    @GetMapping("/courses/register")
    public String showRegistrationCourses(Model model, Principal principal) {
        List<Course> courses = courseService.findAllCourses();
        String userEmail = authService.getCurrentUser().getEmail();
        List<Registration> registrations = registrationService.fetchAll(userEmail);
        RegistrationDto registrationDto = new RegistrationDto();

        model.addAttribute("courses",courses);
        model.addAttribute("registrationDto",registrationDto);
        model.addAttribute("registrations",registrations);

        return "register-courses";
    }

    @PostMapping("/courses/register")
    public String saveRegistration(@Valid @ModelAttribute("registrationDto") RegistrationDto registrationDto, BindingResult result, Model model) {

        Registration alreadyRegistered  = registrationService.findByCourseId(registrationDto.getCourseId());

        if(alreadyRegistered!=null && alreadyRegistered.getId()!=null) {
            result.rejectValue("courseId", null,
                    "You have already registered this course.");

            List<Course> courses = courseService.findAllCourses();
            String userEmail = authService.getCurrentUser().getEmail();
            List<Registration> registrations = registrationService.fetchAll(userEmail);

            model.addAttribute("courses",courses);
            model.addAttribute("registrationDto",registrationDto);
            model.addAttribute("registrations",registrations);

            return "redirect:/courses/register?duplicate";

        }

        registrationService.saveRegistration(registrationDto);

        return "redirect:/courses/register?success";
    }

    @DeleteMapping("/delete/registration/{id}")
    public String deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return "redirect:/courses/register?success";
    }
}
