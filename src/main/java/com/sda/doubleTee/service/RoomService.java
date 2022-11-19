package com.sda.doubleTee.service;

import com.sda.doubleTee.dao.TimeSlot;
import com.sda.doubleTee.dto.AddCourseDto;
import com.sda.doubleTee.dto.AddRoomDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room findByName(String name) {
       return roomRepository.findByName(name);
    }
    public Room findById(Long id) {return roomRepository.findById(id).orElse(null);}

    public void saveRoom(AddRoomDto roomDto) {
        String name = roomDto.getName();
        if(!name.isEmpty()) {
            Room room = new Room();
            room.setName(roomDto.getName());
            roomRepository.save(room);
        }

    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

}
