package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;


    @GetMapping("/teachers/add")
    public String showAddCourses(Model model){
        AddTeacherDto addTeacherDto = new AddTeacherDto();
        List<Teacher> allTeachers = teacherService.findAllTeachers();
        model.addAttribute("addTeacher",addTeacherDto);
        model.addAttribute("teachers",allTeachers);

        return "add-teachers";
    }

    @PostMapping("/teachers/add")
    public String addCourses(@Valid @ModelAttribute("addTeacher") AddTeacherDto addTeacherDto, BindingResult result, Model model) {

        Teacher existingTeacher = teacherService.findById(addTeacherDto.getEmployeeID());

        if(existingTeacher != null && existingTeacher.getId() != null){
            result.rejectValue("name", null,
                    "This room has already been added.");
            List<Teacher> allTeachers = teacherService.findAllTeachers();
            model.addAttribute("addTeacher",addTeacherDto);
            model.addAttribute("teachers",allTeachers);
            return "add-teachers";
        }

        if(result.hasErrors()){
            List<Teacher> allTeachers = teacherService.findAllTeachers();
            model.addAttribute("addCourse",addTeacherDto);
            model.addAttribute("teachers",allTeachers);
            return "add-teachers";
        }

        teacherService.saveTeacher(addTeacherDto);

        return "redirect:/teachers/add?success";
    }

    @GetMapping("/teachers")
    public String displayTeachers(Model model){
        List<Teacher> allTeachers = teacherService.findAllTeachers();
        model.addAttribute("teachers",allTeachers);

        return "teachers";
    }

    @DeleteMapping("/teachers/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return "redirect:/teachers?success";
    }

    }
