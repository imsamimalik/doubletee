package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.Role;
import com.sda.doubleTee.model.User;
import com.sda.doubleTee.repository.RoleRepository;
import com.sda.doubleTee.repository.TimeTableRepository;
import com.sda.doubleTee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void saveUser(UserDto userDto) {
        
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBatch(userDto.getBatch());
        user.setDegree(userDto.getDegree());
        user.setCGPA(userDto.getCGPA());
        user.setDOB(userDto.getDOB());
        user.setPostalAddress(userDto.getPostalAddress());
        user.setEmployeeId(userDto.getEmployeeId());
        user.setDesignation(userDto.getDesignation());

        Role role = roleRepository.findByName(userDto.getRole());
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setName(str[0]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(){
        Role role = new Role();
        role.setName("ROLE_STUDENT");
        return roleRepository.save(role);
    }
}