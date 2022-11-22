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
public class AddAdminDto {

    @NotEmpty(message = "Id cannot be empty")
    private Long id;

    @NotEmpty(message = "name cannot be empty")
    private String name;

}
