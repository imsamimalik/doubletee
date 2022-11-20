package com.sda.doubleTee.controller;

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dao.TimeSlot;
import com.sda.doubleTee.dto.StudentAvailDto;
import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.model.*;
import com.sda.doubleTee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Time;
import java.time.Duration;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Controller
public class TimetableController {

    @Autowired
    TimeTableService timeTableService;
    @Autowired
    CourseService courseService;
    @Autowired
    RoomService roomService;
    @Autowired
    TeacherService teacherService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RegistrationService registrationService;

    @GetMapping("/timetable/add")
    public String allocateTimetable(Model model, String url){

        List<Course> courses = courseService.findAllCourses();
        List<Room> rooms = roomService.findAllRooms();
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Enum> days = Arrays.asList(Days.values());

        TimeTableDto timeTableDto = new TimeTableDto();

        model.addAttribute("courses", courses);
        model.addAttribute("rooms", rooms);
        model.addAttribute("teachers", teachers);
        model.addAttribute("timetableDto",timeTableDto);
        model.addAttribute("days",days);

        return "/allocate-timetable";
    }

    @PostMapping("/timetable/add")
    public String saveAllocationToTT(@Valid @ModelAttribute("timetableDto") TimeTableDto timeTableDto, BindingResult result, Model model) {

        TimeTable teacherClash = timeTableService.findTeacherClash(timeTableDto);
        TimeTable roomClash = timeTableService.findRoomClash(timeTableDto);

        if(teacherClash != null && teacherClash.getTeacher().getId() != null){
            result.rejectValue("teacherId", null,
                    "This teacher is not free at the given time.");

            List<Course> courses = courseService.findAllCourses();
            List<Room> rooms = roomService.findAllRooms();
            List<Teacher> teachers = teacherService.findAllTeachers();
            List<Enum> days = Arrays.asList(Days.values());

            model.addAttribute("courses", courses);
            model.addAttribute("rooms", rooms);
            model.addAttribute("teachers", teachers);
            model.addAttribute("timetableDto",timeTableDto);
            model.addAttribute("days",days);

            return "redirect:/timetable/add?teacherClash";

        }

        if(roomClash != null && roomClash.getRoom().getId() != null){
            result.rejectValue("teacherId", null,
                    "This room is not free at the given time.");

            List<Course> courses = courseService.findAllCourses();
            List<Room> rooms = roomService.findAllRooms();
            List<Teacher> teachers = teacherService.findAllTeachers();
            List<Enum> days = Arrays.asList(Days.values());


            model.addAttribute("courses", courses);
            model.addAttribute("rooms", rooms);
            model.addAttribute("teachers", teachers);
            model.addAttribute("timetableDto",timeTableDto);
            model.addAttribute("days",days);

            return "redirect:/timetable/add?roomClash";
        }
        Duration duration = Duration.between(timeTableDto.getStartTime(),timeTableDto.getEndTime());
        long diff = duration.toMinutes();

        if(diff<=0) {
            result.rejectValue("startTime", null,
                    "The time entered is invalid");

            List<Course> courses = courseService.findAllCourses();
            List<Room> rooms = roomService.findAllRooms();
            List<Teacher> teachers = teacherService.findAllTeachers();
            List<Enum> days = Arrays.asList(Days.values());


            model.addAttribute("courses", courses);
            model.addAttribute("rooms", rooms);
            model.addAttribute("teachers", teachers);
            model.addAttribute("timetableDto",timeTableDto);
            model.addAttribute("days",days);

            return "redirect:/timetable/add?invalidtime";
        }

        timeTableService.addToTimeTable(timeTableDto);

        return "redirect:/timetable/add?success";
    }

    @GetMapping("/timetable")
    public String viewTimetable(Model model) {

        List<TimeTable> timeTables =  timeTableService.fetchAll();

        timeTables.sort(Comparator.comparing(TimeTable::getStartTime));

        model.addAttribute("timeTables", timeTables);
        model.addAttribute("title","University TimeTable");
        return "timetable";
    }

    @DeleteMapping("/timetable/delete/{id}")
    public String deleteTimeTable(@PathVariable Long id) {
        timeTableService.deleteTimeTable(id);
        return "redirect:/timetable?success";
    }




    @GetMapping("/student/empty")
    public String viewStudentAvailability(Model model) {

        StudentAvailDto studentAvailDto = new StudentAvailDto();
        List<Enum> days = Arrays.asList(Days.values());

        model.addAttribute("days",days);
        model.addAttribute("studentAvailDto",studentAvailDto);

        return "student-availability";
    }

    @PostMapping("/student/empty/get")
    public String getStudentAvailability(@Valid @ModelAttribute("studentAvailDto") StudentAvailDto studentAvailDto, BindingResult result, Model model) {

        User student = userService.findByRollNo(studentAvailDto.getRollNumber());
        List<TimeSlot> slots = null;
        if(student!=null) slots =  registrationService.getStudentAvailability(student,studentAvailDto.getDay());

        model.addAttribute("entity",student);
        model.addAttribute("slots",slots);
        model.addAttribute("title","student");

        return "display-availability";

    }


}
