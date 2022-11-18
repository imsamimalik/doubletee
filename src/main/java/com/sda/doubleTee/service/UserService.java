package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<User> findAllUsers();

    void deleteUser(Long id);
}