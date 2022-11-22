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

    @NotNull(message = "id cannot be empty")
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;


    private String department;

}