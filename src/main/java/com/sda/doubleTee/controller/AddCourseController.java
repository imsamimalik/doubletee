package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.AddCourseDto;
import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AddCourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("/add-courses")
    public String showAddCourses(Model model){
        AddCourseDto addCourseDto = new AddCourseDto();
        List<Course> allCourses = courseService.findAllCourses();
        model.addAttribute("addCourse",addCourseDto);
        model.addAttribute("courses",allCourses);
        return "add-courses";
    }

    @PostMapping("/add-courses/save")
    public String addCourses(@Valid @ModelAttribute("addCourse") AddCourseDto addCourseDto, BindingResult result, Model model) {

        Course existingCourse = courseService.findByCodeSection(addCourseDto.getCode(), addCourseDto.getSection());

        if(existingCourse != null && existingCourse.getId() != null){
            result.rejectValue("section", null,
                    "This course code has already been registered for the specific section");
            List<Course> allCourses = courseService.findAllCourses();
            model.addAttribute("addCourse",addCourseDto);
            model.addAttribute("courses",allCourses);
            return "add-courses";
        }

        if(result.hasErrors()){
            List<Course> allCourses = courseService.findAllCourses();
            model.addAttribute("addCourse",addCourseDto);
            model.addAttribute("courses",allCourses);
            return "add-courses";
        }

        courseService.saveCourse(addCourseDto);

        return "redirect:/add-courses?success";
    }


    @GetMapping("/courses")
    public String displayCourses(Model model){
        List<Course> allCourses = courseService.findAllCourses();
        model.addAttribute("courses",allCourses);
        return "courses";
    }



    }
