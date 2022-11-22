package com.sda.doubleTee.controller;

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dao.TimeSlot;
import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.dto.EmptyRoomDto;
import com.sda.doubleTee.dto.FacultyAvailDto;
import com.sda.doubleTee.model.Admin;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.service.AdminService;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.TimeTableService;
import com.sda.doubleTee.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TimeTableService timeTableService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserServiceImpl userService;

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

        Teacher existingTeacher = teacherService.findById(addTeacherDto.getId());
        Admin existingAdmin = adminService.findById(addTeacherDto.getId());
        User existingUser = userService.findByEmployeeId(addTeacherDto.getId());

        if((existingAdmin != null && existingAdmin.getId() != null) ||
                (existingUser != null && existingUser.getId() != null) ||
                (existingTeacher != null && existingTeacher.getId() != null)){
            result.rejectValue("name", null,
                    "This room has already been added.");
            return "redirect:/teachers/add?duplicate";
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


    @GetMapping("/faculty/empty")
    public String viewEmptyRooms(Model model) {

        FacultyAvailDto facultyAvailDto = new FacultyAvailDto();
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Enum> days = Arrays.asList(Days.values());

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
