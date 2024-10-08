package com.sda.doubleTee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Float creditHours;

    @Column(nullable = false)
    private String section;

    @Column(nullable = false)
    private int seats;

    @Column(nullable = false)
    private int maxSeats;


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<TimeTable> allocations;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<Registration> registration;
}
