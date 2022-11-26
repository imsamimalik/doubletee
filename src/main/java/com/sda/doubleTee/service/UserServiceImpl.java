package com.sda.doubleTee.service;

import java.util.Arrays;
import java.util.List;

import com.sda.doubleTee.constants.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.Role;
import com.sda.doubleTee.model.Staff;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.RoleRepository;
import com.sda.doubleTee.repository.StaffRepository;
import com.sda.doubleTee.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public void saveUser(UserDto userDto) {
        
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBatch(userDto.getBatch());
        user.setDegree(userDto.getDegree());
        user.setCGPA(userDto.getCGPA());
        user.setDOB(userDto.getDOB());
        user.setPostalAddress(userDto.getPostalAddress());
        user.setEmployeeId(userDto.getEmployeeId());
        user.setDesignation(userDto.getDesignation());
        user.setRollNumber(userDto.getRollNumber());

        Role role = roleRepository.findByName(userDto.getRole());
        if(role == null){
            role = checkRoleExist(userDto.getRole());
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public void deleteUser(Long id) {
       User user = userRepository.findById(id).orElseThrow();
       user.setRoles(null);
       userRepository.deleteById(id);
    }


    private Role checkRoleExist(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    public User findByRollNo(String rollNo) {
       return userRepository.findByRollNumber(rollNo);
    }

    public User findByEmployeeId(Long id) {
        return userRepository.findByEmployeeId(id);
    }

}