package com.sda.doubleTee.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dto.TimeSlot;
import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.dto.FacultyAvailDto;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.TimeTableService;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TimeTableService timeTableService;


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
        return "redirect:/teachers/add?success";
    }


    @GetMapping("/faculty/empty")
    public String viewEmptyRooms(Model model) {

        FacultyAvailDto facultyAvailDto = new FacultyAvailDto();
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Days> days = Arrays.asList(Days.values());

        model.addAttribute("teachers", teachers);
        model.addAttribute("days",days);
        model.addAttribute("facultyAvailDto",facultyAvailDto);

        return "faculty-availability";
    }

    @PostMapping("/faculty/empty/get")
    public String getEmptyRooms(@Valid @ModelAttribute("facultyAvailDto") FacultyAvailDto facultyAvailDto, BindingResult result, Model model) {

        Teacher teacher = teacherService.findById(facultyAvailDto.getTeacherId());
        List<TimeSlot> slots =  timeTableService.getFacultyAvail(facultyAvailDto.getTeacherId(),facultyAvailDto.getDay());

        model.addAttribute("entity",teacher);
        model.addAttribute("slots",slots);
        model.addAttribute("title","faculty");


        return "display-availability";

    }


}
