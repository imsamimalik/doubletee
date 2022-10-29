package com.sda.doubleTee.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class TimeTableId implements Serializable {

    @Column(name="room_id", nullable = false)
    private Long roomId;

    @Column(name="teacher_id", nullable = false)
    private Long teacherId;

    @Column(name="course_id", nullable = false)
    private Long courseId;

    @Column(name="start_time", nullable = false)
    private LocalTime startTime;

}
