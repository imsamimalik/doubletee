package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Registration;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByName(String name);
}
