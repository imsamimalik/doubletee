package com.sda.doubleTee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timetable")
@IdClass(TimeTableId.class)

public class TimeTable {

    @Id
    @Column(name="room_id", nullable = false)
    private Long roomId;

    @Id
    @Column(name="teacher_id", nullable = false)
    private Long teacherId;

    @Id
    @Column(name="course_id", nullable = false)
    private Long courseId;

    @Id
    @Column(name="start_time", nullable = false)
    private LocalTime startTime;

    @Column(name="end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne
    @JoinColumn(name = "teacher")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "room")
    private Room room;

}
