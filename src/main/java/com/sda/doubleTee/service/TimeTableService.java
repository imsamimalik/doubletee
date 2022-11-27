package com.sda.doubleTee.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sda.doubleTee.dto.TimeSlot;
import com.sda.doubleTee.dto.TimeTableDto;
import com.sda.doubleTee.model.Course;
import com.sda.doubleTee.model.Room;
import com.sda.doubleTee.model.Teacher;
import com.sda.doubleTee.model.TimeTable;
import com.sda.doubleTee.repository.CourseRepository;
import com.sda.doubleTee.repository.RoomRepository;
import com.sda.doubleTee.repository.TeacherRepository;
import com.sda.doubleTee.repository.TimeTableRepository;

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
        saveTimeTable(timeTableDto,allocation);
    }

    private void saveTimeTable(TimeTableDto timeTableDto, TimeTable allocation) {

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

    public void updateTimeTable(TimeTableDto timeTableDto, Long id) {
        TimeTable allocation = timeTableRepository.findById(id).get();
        saveTimeTable(timeTableDto,allocation);
    }

    public List<TimeTable> findAllAllocations() {
        return timeTableRepository.findAll();
    }

    public Long findTeacherClash(TimeTableDto timeTableDto) {
        return timeTableRepository.countByTeacherIdAndDayAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualOrTeacherIdAndDayAndStartTimeLessThanAndEndTimeLessThan(timeTableDto.getTeacherId(), timeTableDto.getDay(), timeTableDto.getStartTime(),timeTableDto.getEndTime(), timeTableDto.getTeacherId(), timeTableDto.getDay(), timeTableDto.getStartTime(),timeTableDto.getEndTime());
    }

    public Long findRoomClash(TimeTableDto timeTableDto) {
        return timeTableRepository.countByRoomIdAndDayAndStartTimeGreaterThanEqualAndStartTimeLessThanEqualOrRoomIdAndDayAndStartTimeLessThanAndEndTimeLessThan(timeTableDto.getRoomId(), timeTableDto.getDay(), timeTableDto.getStartTime(),timeTableDto.getEndTime(), timeTableDto.getRoomId(), timeTableDto.getDay(), timeTableDto.getStartTime(),timeTableDto.getEndTime());
    }
    public List<TimeTable> findByTeacherId(Long id) {
        return timeTableRepository.findByTeacher_Id(id);
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


    public List<TimeSlot> retrieveTimeTable(List<TimeTable> allocations) {
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

    public List<TimeSlot> getEmptyRooms(Long roomId,String day) {
        List<TimeTable> allocations =  timeTableRepository.findByRoom_IdAndDay(roomId, day);
        return retrieveTimeTable(allocations);

    }


    public List<TimeSlot> getFacultyAvail(Long roomId,String day) {
        List<TimeTable> allocations =  timeTableRepository.findByTeacher_IdAndDay(roomId, day);
        return retrieveTimeTable(allocations);
    }

    public TimeTable findById(Long id) {
        return timeTableRepository.findById(id).orElse(null);
    }


    public List<List<TimeTable>> makeSuitableTables(List<String>courses) {

        List<List<TimeTable>> tempTimeTable = new ArrayList<>();

        for (String course : courses) {
            List<TimeTable> temp = timeTableRepository.findByCourse_Name(course);
            tempTimeTable.add(temp);
        }

       List<List<TimeTable>> temp =  UtilityService.cartesianProduct(tempTimeTable);

        List<List<TimeTable>> timetables= new ArrayList<>();

        for (List<TimeTable> tt:temp) {
            tt.sort(Comparator.comparing(TimeTable::getStartTime));
            timetables.add(tt);
        }

        // timetables are sorted wrt start-time

        // After (before endtime - after endtime)
        // in between (after starttime - before endTime)
        // equal (at starttime - at endtime

        boolean clash = false;
        for (int i = 0; i < timetables.size(); i++) {
                List<TimeTable> current = timetables.get(i);
                clash = false;
            for (int j = 0; j < current.size()-1; j++) {
                TimeTable tt = current.get(j);
                for (int k = j+1; k < current.size(); k++) {
                    TimeTable tt2 = current.get(k);
                    if(tt.getDay().equals(tt2.getDay())) {
                        if(tt.getTeacher().equals(tt2.getTeacher())||tt.getRoom().equals(tt2.getRoom())) {
                            if(
                                    (tt2.getStartTime().isBefore(tt.getEndTime()) && tt2.getEndTime().isAfter(tt.getEndTime()))||
                                    (tt2.getStartTime().isAfter(tt.getStartTime()) && tt2.getEndTime().isBefore(tt.getEndTime()))||
                                    (tt2.getStartTime().equals(tt.getStartTime()) && tt2.getEndTime().equals(tt.getEndTime()))
                            ) {
                                clash = true;
                                break;
                            }
                        }
                    }

                }
                if(clash) break;
            }
            if(clash) timetables.remove(current);
        }

        return timetables;

    }


    public Long taughtForDay(Long courseId, String day) {
        return timeTableRepository.countByCourse_IdAndDay(courseId,day);
    }


}
