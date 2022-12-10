package com.sda.doubleTee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sda.doubleTee.model.TimeTable;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    List<TimeTable> findByRoomIdAndDay(Long roomId, String day);
    List<TimeTable> findByTeacherIdAndDay(Long teacherId, String day);

    List<TimeTable> findByTeacher_Id(Long id);
    List<TimeTable> findByDay(String day);

    List<TimeTable> findByCourse_Id(Long id);

    List<TimeTable> findByCourse_Name(String name);

    Long countByCourse_IdAndDay(Long id, String day);

    List<TimeTable> findByCourse_NameAndCourse_Section(String name, String section);

    Long countByRoomId(Long id);
}