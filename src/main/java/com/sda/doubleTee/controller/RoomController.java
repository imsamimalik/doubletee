package com.sda.doubleTee.controller;

import java.util.Arrays;
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

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dto.TimeSlot;
import com.sda.doubleTee.dto.AddRoomDto;
import com.sda.doubleTee.dto.EmptyRoomDto;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.service.RoomService;
import com.sda.doubleTee.service.TimeTableService;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private TimeTableService timeTableService;


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

    @DeleteMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/rooms/add?success";
    }

    @GetMapping("/room/empty")
    public String viewEmptyRooms(Model model) {

        EmptyRoomDto emptyRoomDto = new EmptyRoomDto();
        List<Room> rooms = roomService.findAllRooms();
        List<Days> days = Arrays.asList(Days.values());

        model.addAttribute("rooms", rooms);
        model.addAttribute("days",days);
        model.addAttribute("emptyRoomDto",emptyRoomDto);

        return "empty-rooms";
    }

    @PostMapping("/room/empty/get")
    public String getEmptyRooms(@Valid @ModelAttribute("emptyRoomDto") EmptyRoomDto emptyRoomDto, BindingResult result, Model model) {

        Room room = roomService.findById(emptyRoomDto.getRoomId());
        List<TimeSlot> slots =  timeTableService.getEmptyRooms(emptyRoomDto.getRoomId(),emptyRoomDto.getDay());

        model.addAttribute("entity",room);
        model.addAttribute("slots",slots);
        model.addAttribute("title","room");


        return "display-availability";

    }

}
