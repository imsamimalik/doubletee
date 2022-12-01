package com.sda.doubleTee.service;

import java.util.List;

import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sda.doubleTee.dto.AddTeacherDto;
import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.repository.StaffRepository;
import com.sda.doubleTee.repository.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

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
        teacherRepository.save(teacher);

    }

    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public void deleteTeacher(Long id) {
        User teacher = userRepository.findByEmployeeId(id);
        if(teacher!=null) {
            userService.deleteUser(teacher.getId());
        }
        teacherRepository.deleteById(id);
        staffRepository.deleteById(id);
    }

}
