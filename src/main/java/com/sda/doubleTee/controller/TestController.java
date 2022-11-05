package com.sda.doubleTee.controller;

import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class TestController {

    @Autowired
    TimeTableService timeTableService;

    @GetMapping("/test1")
    public String testFunc(Model model) {
        List<TimeTable> all =  timeTableService.test();
        model.addAttribute("text",all.get(0).getCourse());
        return "hi";
    }
}
