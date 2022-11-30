package com.sda.doubleTee.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    private Float CGPA;
    private int batch;
    private String degree;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date DOB;

    @Column(length = 255)
    private String postalAddress;

    private Long employeeId;
    private String designation;
    private String rollNumber;

    private String role = "ROLE_STUDENT";
}