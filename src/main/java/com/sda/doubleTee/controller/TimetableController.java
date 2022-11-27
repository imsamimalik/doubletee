package com.sda.doubleTee.controller;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.sda.doubleTee.dto.PersonalizedTTDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dto.CSVTT;
import com.sda.doubleTee.dto.TimeSlot;
import com.sda.doubleTee.dto.StudentAvailDto;
import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.service.CourseService;
import com.sda.doubleTee.service.RegistrationService;
import com.sda.doubleTee.service.RoomService;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.TimeTableService;
import com.sda.doubleTee.service.UserServiceImpl;

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
    public String allocateTimetable(Model model){

        List<Course> courses = courseService.findAllCourses();
        List<Room> rooms = roomService.findAllRooms();
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Days> days = Arrays.asList(Days.values());

        TimeTableDto timeTableDto = new TimeTableDto();

        model.addAttribute("courses", courses);
        model.addAttribute("rooms", rooms);
        model.addAttribute("teachers", teachers);
        model.addAttribute("timetableDto",timeTableDto);
        model.addAttribute("days",days);
        model.addAttribute("title","Allocate TimeTable");

        return "/allocate-timetable";
    }

    @PostMapping("/timetable/add")
    public String saveAllocationToTT(@Valid @ModelAttribute("timetableDto") TimeTableDto timeTableDto, BindingResult result, Model model) {

        Long teacherClash = timeTableService.findTeacherClash(timeTableDto);
        Long roomClash = timeTableService.findRoomClash(timeTableDto);

        if(teacherClash > 0){
            result.rejectValue("teacherId", null,
                    "This teacher is not free at the given time.");

            return "redirect:/timetable/add?teacherClash";

        }

        if(roomClash > 0){
            result.rejectValue("teacherId", null,
                    "This room is not free at the given time.");
            return "redirect:/timetable/add?roomClash";
        }
        Course course = courseService.findById(timeTableDto.getCourseId());
        Room room = roomService.findById(timeTableDto.getRoomId());

        if(course.getMaxSeats() > room.getCapacity()) {
            return "redirect:/timetable/add?space";
        }

        Duration duration = Duration.between(timeTableDto.getStartTime(),timeTableDto.getEndTime());
        long diff = duration.toMinutes();

        if(diff<=0) {
            result.rejectValue("startTime", null,
                    "The time entered is invalid");

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
        List<Days> days = Arrays.asList(Days.values());

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

    @GetMapping("/timetable/download")
    public void downloadCSV(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        String filename = "timetable.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<CSVTT> writer = new StatefulBeanToCsvBuilder<CSVTT>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        List<TimeTable> tt = timeTableService.fetchAll();
        List<CSVTT> formattedTT = tt.stream().map(t->{
            return new CSVTT(t.getId(), t.getCourse().getId(),t.getTeacher().getId(),t.getRoom().getId(),t.getStartTime(),t.getEndTime(),t.getDay());
        }).toList();

        //write to csv file
        writer.write(formattedTT);
    }

    @GetMapping("/timetable/personalized")
    public String viewPersonalizedTimetable(Model model) {

        PersonalizedTTDto personalizedTTDto = new PersonalizedTTDto();

        List<Course> tempCourses = courseService.findAllCourses();

        List<String> courses = tempCourses.stream().map(c->c.getName()).distinct().toList();


        model.addAttribute("courses", courses);
        model.addAttribute("personalizedTTDto", personalizedTTDto);
        return "personalized-timetable";
    }


    @PostMapping("/timetable/personalized/get")
    public String calcPersonalizedTT(@Valid @ModelAttribute("personalizedTTDto") PersonalizedTTDto personalizedTTDto, BindingResult result, Model model) {

        List<String> courses = personalizedTTDto.getSubjects().stream()
                .filter(str -> str!=null).distinct()
                .collect(Collectors.toList());

        List<List<TimeTable>> timetables = timeTableService.makeSuitableTables(courses);

        List<Integer> count = new ArrayList<>(courses.size());
       timetables.stream().forEach(tempTables -> {
            List<String> days = new ArrayList<>();
           tempTables.stream().forEach(tt -> days.add(tt.getDay()));
           count.add(new HashSet<String>(days).size());
       }

       );


        model.addAttribute("timetables", timetables);
        model.addAttribute("count",count);
        return "view-personalized-tt";

    }


    @GetMapping("/timetable/edit/{id}")
    public String editTimetable(@PathVariable Long id, Model model){

        List<Course> courses = courseService.findAllCourses();
        List<Room> rooms = roomService.findAllRooms();
        List<Teacher> teachers = teacherService.findAllTeachers();
        List<Days> days = Arrays.asList(Days.values());

        TimeTable temp = timeTableService.findById(id);

        TimeTableDto timeTableDto = new TimeTableDto();
        timeTableDto.setCourseId(temp.getCourse().getId());
        timeTableDto.setTeacherId(temp.getTeacher().getId());
        timeTableDto.setRoomId(temp.getRoom().getId());
        timeTableDto.setStartTime(temp.getStartTime());
        timeTableDto.setEndTime(temp.getEndTime());
        timeTableDto.setDay(temp.getDay());

        model.addAttribute("courses", courses);
        model.addAttribute("rooms", rooms);
        model.addAttribute("teachers", teachers);
        model.addAttribute("timetableDto",timeTableDto);
        model.addAttribute("days",days);
        model.addAttribute("id",temp.getId());
        model.addAttribute("title","Edit TimeTable");

        return "/allocate-timetable";
    }

    @PutMapping("/timetable/edit/save/{id}")
    public String updateTimetable(@PathVariable Long id, @Valid @ModelAttribute("timetableDto") TimeTableDto timeTableDto, BindingResult result) {

        Long teacherClash = timeTableService.findTeacherClash(timeTableDto);
        Long roomClash = timeTableService.findRoomClash(timeTableDto);

        if(teacherClash > 0){
            result.rejectValue("teacherId", null,
                    "This teacher is not free at the given time.");

            return "redirect:/timetable/edit/"+id+"?teacherClash";

        }

        if(roomClash > 0){
            result.rejectValue("teacherId", null,
                    "This room is not free at the given time.");
            return "redirect:/timetable/edit/"+id+"?roomClash";

        }
        Course course = courseService.findById(timeTableDto.getCourseId());
        Room room = roomService.findById(timeTableDto.getRoomId());

        if(course.getMaxSeats() > room.getCapacity()) {
            return "redirect:/timetable/edit/"+id+"?space";
        }

        Duration duration = Duration.between(timeTableDto.getStartTime(),timeTableDto.getEndTime());
        long diff = duration.toMinutes();

        if(diff<=0) {
            result.rejectValue("startTime", null,
                    "The time entered is invalid");
            return "redirect:/timetable/edit/"+id+"?invalidtime";
        }

        timeTableService.updateTimeTable(timeTableDto, id);

        return "redirect:/timetable?success";

    }


    }
