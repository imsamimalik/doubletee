package com.sda.doubleTee.controller;

import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.service.AuthService;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class TestController {

    @Autowired
    private AuthService authService;

    @GetMapping("/hello")
    public String hello(@CurrentSecurityContext(expression="authentication?.name")
                        String username, Model model) {
        model.addAttribute("name", username);
        return "hi";
    }
}
