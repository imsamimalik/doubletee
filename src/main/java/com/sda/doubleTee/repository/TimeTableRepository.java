package com.sda.doubleTee.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sda.doubleTee.model.TimeTable;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    TimeTable findByRoomIdAndDayAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long roomId, String day, LocalTime startTime, LocalTime endTime);
    TimeTable findByTeacherIdAndDayAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Long teacherId, String day, LocalTime startTime, LocalTime endTime);
    List<TimeTable> findByTeacher_Id(Long id);
    List<TimeTable> findByDay(String day);

    List<TimeTable> findByCourse_Id(Long id);

    List<TimeTable> findByCourse_Name(String name);

    List<TimeTable> findByRoom_IdAndDay(Long id, String day);

    List<TimeTable> findByTeacher_IdAndDay(Long id, String day);

}