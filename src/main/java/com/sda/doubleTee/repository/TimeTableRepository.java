package com.sda.doubleTee.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sda.doubleTee.model.TimeTable;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    Long countByRoomIdAndDayAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualOrRoomIdAndDayAndStartTimeLessThanAndEndTimeLessThan(Long roomId1, String day1, LocalTime startTime1, LocalTime endTime1, Long roomId2, String day2, LocalTime startTime2, LocalTime endTime2);
    Long countByTeacherIdAndDayAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualOrTeacherIdAndDayAndStartTimeLessThanAndEndTimeLessThan(Long teacherId1, String day1, LocalTime startTime1, LocalTime endTime1, Long teacherId2, String day2, LocalTime startTime2, LocalTime endTime2);
    List<TimeTable> findByTeacher_Id(Long id);
    List<TimeTable> findByDay(String day);

    List<TimeTable> findByCourse_Id(Long id);

    List<TimeTable> findByCourse_Name(String name);

    List<TimeTable> findByRoom_IdAndDay(Long id, String day);

    List<TimeTable> findByTeacher_IdAndDay(Long id, String day);


}