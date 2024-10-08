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
import static java.time.temporal.ChronoUnit.MINUTES;

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

    public boolean findTeacherClash(TimeTableDto timeTableDto,Long id) {
        List<TimeTable> timeTables =  timeTableRepository.findByTeacherIdAndDay(timeTableDto.getTeacherId(), timeTableDto.getDay());

        if(id!=null) {
            for (int i = 0; i < timeTables.size(); i++) {
                if(timeTables.get(i).getId()==id) timeTables.remove(timeTables.get(i));
            }
        }

        return checkClash(timeTables,timeTableDto.getStartTime(),timeTableDto.getEndTime());
    }

    public boolean findRoomClash(TimeTableDto timeTableDto, Long id) {
        List<TimeTable> timeTables =  timeTableRepository.findByRoomIdAndDay(timeTableDto.getRoomId(), timeTableDto.getDay());

        if(id!=null) {
            for (int i = 0; i < timeTables.size(); i++) {
                if(timeTables.get(i).getId()==id) timeTables.remove(timeTables.get(i));
            }
        }

        return checkClash(timeTables,timeTableDto.getStartTime(),timeTableDto.getEndTime());
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
                if(MINUTES.between(start,slot.getStartTime()) >10 ) {
                    freeSlots.add(new TimeSlot(start,slot.getStartTime()));
                }
            }
            else if(!slots.get(i-1).getEndTime().equals(slot.getStartTime())){
                if(MINUTES.between(slots.get(i-1).getEndTime(),slot.getStartTime()) > 10) {
                    freeSlots.add(new TimeSlot(slots.get(i-1).getEndTime(), slot.getStartTime()));
                }
            }
        }

        if(MINUTES.between(slots.get(slots.size()-1).getEndTime(), end) > 10) {
            freeSlots.add(new TimeSlot(slots.get(slots.size()-1).getEndTime(),end));
        }

        return freeSlots;
    }

    public List<TimeSlot> getEmptyRooms(Long roomId,String day) {
        List<TimeTable> allocations =  timeTableRepository.findByRoomIdAndDay(roomId, day);
        return retrieveTimeTable(allocations);

    }


    public List<TimeSlot> getFacultyAvail(Long roomId,String day) {
        List<TimeTable> allocations =  timeTableRepository.findByTeacherIdAndDay(roomId, day);
        return retrieveTimeTable(allocations);
    }

    public TimeTable findById(Long id) {
        return timeTableRepository.findById(id).orElse(null);
    }


    public List<List<TimeTable>> makeSuitableTables(List<String>courses) {

        List<List<TimeTable>> tempTimeTable = new ArrayList<>();

        for (String course : courses) {
            List<TimeTable> temp = timeTableRepository.findByCourse_Name(course);
            for (int i = 0; i < temp.size(); i++) {
                TimeTable tt = temp.get(i);
                for (int j = i+1; j < temp.size(); j++) {
                    TimeTable tt2 = temp.get(j);
                    if(tt2.getCourse().equals(tt.getCourse()) && !tt2.getDay().equals(tt.getDay())) {
                        temp.remove(tt2);
                        break;
                    }
                }
            }
            tempTimeTable.add(temp);
        }

       List<List<TimeTable>> temp =  UtilityService.cartesianProduct(tempTimeTable);
        for (int i = 0; i < temp.size(); i++) {
            List<TimeTable> list = temp.get(i);
            int size = list.size();
            for (int j = 0; j < size; j++) {
                TimeTable tt = list.get(j);
                if(tt.getDay().equals("Monday")||tt.getDay().equals("Tuesday")||tt.getDay().equals("Wednesday")) {
                    List<TimeTable> courseTT = timeTableRepository.findByCourse_NameAndCourse_Section(tt.getCourse().getName(),tt.getCourse().getSection());
                    for (TimeTable ctt:courseTT) {
                        if(!ctt.getDay().equals(tt.getDay()))
                            list.add(ctt);
                    }
                }
            }
        }

        List<List<TimeTable>> timetables= new ArrayList<>();

        for (List<TimeTable> tt:temp) {
            tt.sort(Comparator.comparing(TimeTable::getStartTime));
            timetables.add(tt);
        }

        System.out.println("LEEEENNNFGTHHH,"+timetables.size());

        // timetables are sorted wrt start-time

        // After (before endtime - after endtime)
        // in between (after starttime - before endTime)
        // equal (at starttime - at endtime
        boolean clash = false;
        int m = 0;
        while(m<10) {

        for (int i = 0; i < timetables.size(); i++) {
                List<TimeTable> current = timetables.get(i);
            for (int j = 0; j < current.size(); j++) {
                clash = false;
                TimeTable tt = current.get(j);
                for (int k = j+1; k < current.size(); k++) {
                    TimeTable tt2 = current.get(k);
                    if(tt.getDay().equals(tt2.getDay())) {
                        if (overLap(tt.getStartTime(), tt.getEndTime(), tt2.getStartTime(), tt2.getEndTime())) {
                                clash = true;
                                break;
                            }
                    }

                }
                if(clash) break;
            }
            if(clash) timetables.remove(current);
        }
        m++;
        }


        return timetables;

    }


    public Long taughtForDay(Long courseId, String day) {
        return timeTableRepository.countByCourse_IdAndDay(courseId,day);
    }

    private boolean checkClash(List<TimeTable> timeTables, LocalTime start, LocalTime end) {
        for (TimeTable temp: timeTables) {
            if(overlap(temp.getStartTime(),temp.getEndTime(), start,end)) {
                return true;
            }
        }
        return false;
    }

    private boolean overlap (LocalTime startA, LocalTime endA, LocalTime startB, LocalTime endB)
    {
        return (endB == null || startA == null || !startA.isAfter(endB))
                && (endA == null || startB == null || !endA.isBefore(startB));
    }
    public boolean overLap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {

        return (start1.equals(start2) || end1.equals(end2) || (start1.isBefore(end2) && start2.isBefore(end1)));

    }

    public boolean isRoomAllocated(Long id) {
       Long count = timeTableRepository.countByRoomId(id);
       if(count>0) return true;
       else return false;
    }



}
