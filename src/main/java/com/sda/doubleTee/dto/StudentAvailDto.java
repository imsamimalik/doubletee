package com.sda.doubleTee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentAvailDto {

    @NotEmpty(message = "Day cannot be empty")
    private String day;

    @NotEmpty(message = "Roll Number cannot be empty")
    private String rollNumber;
}
