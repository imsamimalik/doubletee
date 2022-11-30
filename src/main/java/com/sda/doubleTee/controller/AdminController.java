package com.sda.doubleTee.controller;

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

import com.sda.doubleTee.dto.AddAdminDto;
import com.sda.doubleTee.model.Admin;
import com.sda.doubleTee.service.AdminService;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/admins")
    public String showAdmins(Model model) {

        List<Admin> adminList = adminService.fetchAll();
        AddAdminDto addAdminDto = new AddAdminDto();

        model.addAttribute("admins",adminList);
        model.addAttribute("addAdminDto",addAdminDto);
        return "admins";
    }

    @PostMapping("/admins")
    public String addCourses(@Valid @ModelAttribute("addAdminDto") AddAdminDto addAdminDto, BindingResult result) {

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
