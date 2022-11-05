package com.sda.doubleTee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimeTableDto {

    @NotNull(message = "Room cannot be empty")
    private Long roomId;

    @NotNull(message = "Teacher cannot be empty")
    private Long teacherId;

    @NotNull(message = "Course cannot be empty")
    private Long courseId;

    @NotNull(message = "Start time cannot be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime startTime;

    @NotNull(message = "End Time cannot be empty")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime endTime;

    @NotNull(message = "Weekday cannot be empty")
    private String day;
}