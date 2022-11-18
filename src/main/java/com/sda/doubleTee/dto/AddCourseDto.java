package com.sda.doubleTee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCourseDto {

    private Long id;

    @NotEmpty(message = "Code cannot be empty")
    private String code;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull
    private Float creditHours;

    @NotEmpty(message = "Section cannot be empty")
    private String section;

    @NotEmpty(message = "Capacity cannot be empty")
    private int capacity;

}