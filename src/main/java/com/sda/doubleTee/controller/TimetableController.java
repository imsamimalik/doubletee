package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.repository.TeacherRepository;
import com.sda.doubleTee.repository.TimeTableRepository;
import com.sda.doubleTee.service.CourseService;
import com.sda.doubleTee.service.RoomService;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.TimeTableService;
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
public class TimetableController {

    @Autowired
    TimeTableService timeTableService;
    @Autowired
    CourseService courseService;
    @Autowired
    RoomService roomService;
    @Autowired
    TeacherService teacherService;


    @GetMapping("/allocate")
    public String allocateTimetable(Model model, String url){

        List<Course> courses = courseService.findAllCourses();
        List<Room> rooms = roomService.findAllRooms();
        List<Teacher> teachers = teacherService.findAllTeachers();

        TimeTableDto timeTableDto = new TimeTableDto();

        model.addAttribute("courses", courses);
        model.addAttribute("rooms", rooms);
        model.addAttribute("teachers", teachers);
        model.addAttribute("timetableDto",timeTableDto);

        return "/allocate-timetable";
    }

    @PostMapping("/allocate")
    public String saveAllocationToTT(@Valid @ModelAttribute("timetableDto") TimeTableDto timeTableDto, BindingResult result, Model model) {

        TimeTable teacherClash = timeTableService.findTeacherClash(timeTableDto);
        TimeTable roomClash = timeTableService.findRoomClash(timeTableDto);

        if(teacherClash != null && teacherClash.getTeacherId() != null){
            result.rejectValue("teacherId", null,
                    "This teacher is not free at the given time.");

            List<Course> courses = courseService.findAllCourses();
            List<Room> rooms = roomService.findAllRooms();
            List<Teacher> teachers = teacherService.findAllTeachers();


            model.addAttribute("courses", courses);
            model.addAttribute("rooms", rooms);
            model.addAttribute("teachers", teachers);
            model.addAttribute("timetableDto",timeTableDto);

            return "redirect:/allocate?teacherClash";

        }

        if(roomClash != null && roomClash.getRoomId() != null){
            result.rejectValue("teacherId", null,
                    "This room is not free at the given time.");

            List<Course> courses = courseService.findAllCourses();
            List<Room> rooms = roomService.findAllRooms();
            List<Teacher> teachers = teacherService.findAllTeachers();


            model.addAttribute("courses", courses);
            model.addAttribute("rooms", rooms);
            model.addAttribute("teachers", teachers);
            model.addAttribute("timetableDto",timeTableDto);

            return "redirect:/allocate?roomClash";
        }

        timeTableService.addToTimeTable(timeTableDto);

        return "redirect:/allocate?success";
    }


}
