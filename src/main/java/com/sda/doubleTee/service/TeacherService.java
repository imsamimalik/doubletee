package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.AddRoomDto;
import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.repository.RoomRepository;
import com.sda.doubleTee.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public Teacher findById(String id) {
       return teacherRepository.findByEmployeeID(id);
    }

    public void saveTeacher(AddTeacherDto roomDto) {

        Teacher room = new Teacher();
        room.setName(roomDto.getName());
        room.setEmployeeID(roomDto.getEmployeeID());
        room.setDepartment(roomDto.getDepartment());
        teacherRepository.save(room);

    }

    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

}
