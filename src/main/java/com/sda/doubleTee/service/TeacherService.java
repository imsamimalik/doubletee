package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.AddRoomDto;
import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.repository.RoomRepository;
import com.sda.doubleTee.repository.StaffRepository;
import com.sda.doubleTee.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StaffRepository staffRepository;

    public Teacher findById(Long id) {
       return teacherRepository.findById(id).orElse(null);
    }

    public void saveTeacher(AddTeacherDto teacherDto) {

        Teacher teacher = new Teacher();
        Staff staff = new Staff();
        Staff newStaff = staffRepository.save(staff);
        teacher.setId(newStaff.getId());
        teacher.setName(teacherDto.getName());
        teacher.setDepartment(teacherDto.getDepartment());

        System.out.printf("UDDDDDDDDDDDDDD %d%n", teacher.getId());

        teacherRepository.save(teacher);

    }

    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
        staffRepository.deleteById(id);
    }

}
