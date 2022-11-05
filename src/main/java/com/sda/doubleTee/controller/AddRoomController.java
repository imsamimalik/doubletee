package com.sda.doubleTee.controller;

import com.sda.doubleTee.dto.AddRoomDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.service.RoomService;
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
public class AddRoomController {

    @Autowired
    private RoomService roomService;


    @GetMapping("/rooms/add")
    public String showAddCourses(Model model){
        AddRoomDto addRoomDto = new AddRoomDto();
        List<Room> allRooms = roomService.findAllRooms();
        model.addAttribute("addRoom",addRoomDto);
        model.addAttribute("rooms",allRooms);

        return "add-rooms";
    }

    @PostMapping("/rooms/add")
    public String addCourses(@Valid @ModelAttribute("addRoom") AddRoomDto addRoomDto, BindingResult result, Model model) {

        Room existingRoom = roomService.findByName(addRoomDto.getName());

        if(existingRoom != null && existingRoom.getId() != null){
            result.rejectValue("name", null,
                    "This room has already been added.");
            List<Room> allRooms = roomService.findAllRooms();
            model.addAttribute("addRoom",addRoomDto);
            model.addAttribute("rooms",allRooms);
            return "add-rooms";
        }

        if(result.hasErrors()){
            List<Room> allRooms = roomService.findAllRooms();
            model.addAttribute("addCourse",addRoomDto);
            model.addAttribute("courses",allRooms);
            return "add-rooms";
        }

        roomService.saveRoom(addRoomDto);

        return "redirect:/rooms/add?success";
    }

    @GetMapping("/rooms")
    public String displayRooms(Model model){
        List<Room> allRooms = roomService.findAllRooms();
        model.addAttribute("rooms",allRooms);

        return "rooms";
    }

    }
