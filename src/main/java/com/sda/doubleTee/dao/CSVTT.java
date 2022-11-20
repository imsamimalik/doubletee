package com.sda.doubleTee.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CSVTT {
    private Long id;
    private Long course;
    private Long teacher;
    private Long room;
    private LocalTime startTime;
    private LocalTime endTime;
    private String day;
}
