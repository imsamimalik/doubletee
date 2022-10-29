package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.model.TimeTableId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, TimeTableId> {

    TimeTable findByRoomIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long roomId, LocalTime startTime, LocalTime endTime);
    TimeTable findByTeacherIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long teacherId, LocalTime startTime, LocalTime endTime);

}