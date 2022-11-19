package com.sda.doubleTee.controller;

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dao.TimeSlot;
import com.sda.doubleTee.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class TestController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/hello")
    public String hello( Model model) {

        return "index";
    }
}
