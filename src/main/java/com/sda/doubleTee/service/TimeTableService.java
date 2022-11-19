package com.sda.doubleTee.service;

import com.sda.doubleTee.constants.Days;
import com.sda.doubleTee.dao.TimeSlot;
import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.dto.UserDto;
import com.sda.doubleTee.model.*;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RoomRepository;
import com.sda.doubleTee.repository.TeacherRepository;
import com.sda.doubleTee.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public  List<TimeTable> fetchAll() {
        return timeTableRepository.findAll();
    }

    public List<TimeTable> getByCourseIds(List<Long> courses) {

        List<TimeTable> timeTables = new ArrayList<>();;

        for (Long course:courses) {
            List<TimeTable> temp = timeTableRepository.findByCourse_Id(course);
            for (TimeTable tt:temp) {
            timeTables.add(tt);
            }
        }
        return timeTables;
    }

    public void deleteTimeTable(Long id) {
        timeTableRepository.deleteById(id);
    }

    public List<TimeSlot> getEmptyRooms(Long roomId,String day) {
        List<TimeTable> allocations =  timeTableRepository.findByRoom_IdAndDay(roomId, day);

        List<TimeSlot> slots = new ArrayList<>(allocations.stream().map(t -> {
            return new TimeSlot(t.getStartTime(), t.getEndTime());
        }).toList());

        slots.sort(Comparator.comparing(TimeSlot::getStartTime));

        List<TimeSlot> freeSlots = new ArrayList<TimeSlot>();
        LocalTime start = LocalTime.of(8,0,0);
        LocalTime end = LocalTime.of(21,0,0);

        if(slots.size()==0) {
            freeSlots.add(new TimeSlot(start,end));
            return freeSlots;
        }

        for (int i = 0; i<slots.size(); i++) {
            TimeSlot slot  = slots.get(i);

            if(i==0 && !start.equals(slot)) {
                freeSlots.add(new TimeSlot(start,slot.getStartTime()));
            }
            else if(!slots.get(i-1).getEndTime().equals(slot.getStartTime())){
                freeSlots.add(new TimeSlot(slots.get(i-1).getEndTime(), slot.getStartTime()));
            }
        }


        freeSlots.add(new TimeSlot(slots.get(slots.size()-1).getEndTime(),end));

        return freeSlots;

    }

}
