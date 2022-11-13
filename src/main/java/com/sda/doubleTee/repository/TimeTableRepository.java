package com.sda.doubleTee.repository;

import com.sda.doubleTee.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    TimeTable findByRoomIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long roomId, LocalTime startTime, LocalTime endTime);
    TimeTable findByTeacherIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long teacherId, LocalTime startTime, LocalTime endTime);
    List<TimeTable> findByTeacher_Id(Long id);
    List<TimeTable> findByDay(String day);

}