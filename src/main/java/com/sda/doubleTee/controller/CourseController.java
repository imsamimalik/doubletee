package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.AddCourseDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;


    @GetMapping("/courses/add")
    public String showAddCourses(Model model){
        AddCourseDto addCourseDto = new AddCourseDto();
        List<Course> allCourses = courseService.findAllCourses();
        model.addAttribute("addCourse",addCourseDto);
        model.addAttribute("courses",allCourses);
        return "add-courses";
    }

    @PostMapping("/courses/add")
    public String addCourses(@Valid @ModelAttribute("addCourse") AddCourseDto addCourseDto, BindingResult result, Model model) {

        Course existingCourse = courseService.findByCodeSection(addCourseDto.getCode(), addCourseDto.getSection());

        if(existingCourse != null && existingCourse.getId() != null){
            result.rejectValue("section", null,
                    "This course code has already been registered for the specific section");
            return "redirect:/courses/add?error";
        }

        if(result.hasErrors()){
            return "redirect:/courses/add?error";
        }

        courseService.saveCourse(addCourseDto);

        return "redirect:/courses/add?success";
    }


    @GetMapping("/courses")
    public String displayCourses(Model model){
        List<Course> allCourses = courseService.findAllCourses();
        model.addAttribute("courses",allCourses);
        return "courses";
    }


    @DeleteMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/courses/add?success";
    }

    }
