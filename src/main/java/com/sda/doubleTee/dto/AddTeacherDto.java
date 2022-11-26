package com.sda.doubleTee.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddTeacherDto {

    @NotEmpty(message = "Name cannot be empty")
    private String name;


    private String department;

}