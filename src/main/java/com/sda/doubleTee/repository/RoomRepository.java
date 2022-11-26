package com.sda.doubleTee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sda.doubleTee.model.Room;


public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String name);
}
