package com.sda.doubleTee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddTeacherDto {

    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull
    private String employeeID;

    private String department;

}