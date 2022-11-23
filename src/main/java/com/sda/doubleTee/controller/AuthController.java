package com.sda.doubleTee.controller;

import com.sda.doubleTee.constants.Roles;
import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.Admin;
import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.StaffRepository;
import com.sda.doubleTee.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private StaffService staffService;


    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true, 10));
    }


    @GetMapping("/register")
    public String redirectRegistration(){
        return "redirect:/register/student";
    }

    // handler method to handle user registration form request
    @GetMapping("/register/student")
    public String showRegistrationStudent(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            UserDto user = new UserDto();
            user.setRole(Roles.STUDENT.getRole());
            model.addAttribute("title", "student");
            model.addAttribute("user", user);
            return "register";
        }

        return "redirect:/";

    }


    @GetMapping("/register/faculty")
    public String showRegistrationFaculty(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            UserDto user = new UserDto();
            user.setRole(Roles.FACULTY.getRole());
            model.addAttribute("title", "faculty");
            model.addAttribute("user", user);
            return "register";
        }

        return "redirect:/";

    }


    @GetMapping("/register/admin")
    public String showRegistrationAdmin(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            UserDto user = new UserDto();
            user.setRole(Roles.ADMIN.getRole());
            model.addAttribute("title", "admin");
            model.addAttribute("user", user);
            return "register";
        }

        return "redirect:/";

    }


    // handler method to handle user registration form submit request
    @PostMapping("/register")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, Model model,BindingResult result){
        User existingUser =null;
        if(!userDto.getRole().equals(Roles.STUDENT.getRole())) {
            existingUser = userService.findByEmployeeId(userDto.getEmployeeId());
        }else{
            existingUser = userService.findByRollNo(userDto.getRollNumber());
        }


        if(existingUser != null){
            result.rejectValue("id", null,
                    "This id has already been added.");
            return redirectByRole("register",userDto.getRole().substring(5).toLowerCase(),"found");
        }


        Long employeeId = userDto.getEmployeeId();
        if(employeeId!=null) {
            Staff existingStaff = staffService.findById(userDto.getEmployeeId());
            if(existingStaff==null) {
                return redirectByRole("register",userDto.getRole().substring(5).toLowerCase(),"notfound");
            }
        }


        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return redirectByRole("register",userDto.getRole().substring(5).toLowerCase(),"error");
        }

        userService.saveUser(userDto);
        return redirectByRole("register",userDto.getRole().substring(5).toLowerCase(),"success");

    }

    // handler method to handle list of users
    @GetMapping("/")
    public String users(Model model){

        Authentication auth = authService.getAuth();

        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
           return "redirect:/login/student";
        }

        boolean hasAdminRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if(hasAdminRole) {
            List<User> users = userService.findAllUsers();
            model.addAttribute("users", users);
            return "users";
        }

        return "redirect:/my-timetable";
    }

    // handler method to handle login request
    @GetMapping("/login/student")
    public String loginStudent(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("title", "student");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/login/faculty")
    public String loginFaculty(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("title", "faculty");
            return "login";
        }

        return "redirect:/";
    }

    @GetMapping("/login/admin")
    public String loginAdmin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("title", "admin");
            return "login";
        }

        return "redirect:/";
    }
    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/";
    }

    private String redirectByRole(String path, String role, String message) {
        return "redirect:/"+path+ "/" + role + "?" + message;
    }
}

