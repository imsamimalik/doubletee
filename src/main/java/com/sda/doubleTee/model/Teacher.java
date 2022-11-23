package com.sda.doubleTee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private String department;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    List<TimeTable> allocations;
}
