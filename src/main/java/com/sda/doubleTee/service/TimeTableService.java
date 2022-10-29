package com.sda.doubleTee.service;

import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.*;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RoomRepository;
import com.sda.doubleTee.repository.TeacherRepository;
import com.sda.doubleTee.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
       // TimeTableId id = new TimeTableId(timeTableDto.getRoomId(), timeTableDto.getTeacherId(), timeTableDto.getCourseId(), timeTableDto.getStartTime());
        //allocation.setId(id);
        allocation.setRoomId(timeTableDto.getRoomId());
        allocation.setTeacherId(timeTableDto.getTeacherId());
        allocation.setCourseId(timeTableDto.getCourseId());
        allocation.setStartTime(timeTableDto.getStartTime());
        allocation.setEndTime(timeTableDto.getEndTime());

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
}
