package com.sda.doubleTee.service;

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.*;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RoomRepository;
import com.sda.doubleTee.repository.TeacherRepository;
import com.sda.doubleTee.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Arrays;
import java.util.List;

@Service
public class TimeTableService {

    @Autowired
    private TimeTableRepository timeTableRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    public void addToTimeTable(TimeTableDto timeTableDto) {

        TimeTable allocation  = new TimeTable();
        allocation.setStartTime(timeTableDto.getStartTime());
        allocation.setEndTime(timeTableDto.getEndTime());
        allocation.setDay(timeTableDto.getDay());

        Course course = courseRepository.findById(timeTableDto.getCourseId()).orElse(null);
        allocation.setCourse(course);

        Room room = roomRepository.findById(timeTableDto.getRoomId()).orElse(null);
        allocation.setRoom(room);

        Teacher teacher = teacherRepository.findById(timeTableDto.getTeacherId()).orElse(null);
        allocation.setTeacher(teacher);

        timeTableRepository.save(allocation);
    }

    public List<TimeTable> findAllAllocations() {
        return timeTableRepository.findAll();
    }

    public TimeTable findTeacherClash(TimeTableDto timeTableDto) {
        return timeTableRepository.findByTeacherIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(timeTableDto.getTeacherId(),timeTableDto.getStartTime(),timeTableDto.getEndTime());
    }

    public TimeTable findRoomClash(TimeTableDto timeTableDto) {
        return timeTableRepository.findByRoomIdAndStartTimeGreaterThanEqualAndStartTimeLessThanEqual(timeTableDto.getRoomId(),timeTableDto.getStartTime(),timeTableDto.getEndTime());
    }
    //test
    public List<TimeTable> test() {
        return timeTableRepository.findByTeacher_Id(Long.valueOf(2));
    }

    public List<TimeTable> findByDay(String day) {
        return timeTableRepository.findByDay(day);
    }

    public void deleteTimeTable(Long id) {
        timeTableRepository.deleteById(id);
    }

}
