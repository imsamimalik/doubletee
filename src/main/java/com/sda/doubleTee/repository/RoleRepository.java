package com.sda.doubleTee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sda.doubleTee.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}