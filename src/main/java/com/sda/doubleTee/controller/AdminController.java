package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.AddAdminDto;
import com.sda.doubleTee.dto.AddRoomDto;
import com.sda.doubleTee.model.Admin;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.service.AdminService;
import com.sda.doubleTee.service.TeacherService;
import com.sda.doubleTee.service.UserService;
import com.sda.doubleTee.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/admins")
    public String showAdmins(Model model) {

        List<Admin> adminList = adminService.fetchAll();
        AddAdminDto addAdminDto = new AddAdminDto();

        model.addAttribute("admins",adminList);
        model.addAttribute("addAdminDto",addAdminDto);
        return "admins";
    }

    @PostMapping("/admins")
    public String addCourses(@Valid @ModelAttribute("addAdminDto") AddAdminDto addAdminDto, BindingResult result, Model model) {

        if(result.hasErrors())  return "redirect:/admins";


        adminService.saveAdmin(addAdminDto);

        return "redirect:/admins?success";
    }


    @DeleteMapping("/admins/delete/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return "redirect:/admins?success";
    }
}
