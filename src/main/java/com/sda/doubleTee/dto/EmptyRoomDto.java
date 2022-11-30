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
public class EmptyRoomDto {

    @NotEmpty(message = "Day cannot be empty")
    private String day;

    @NotEmpty(message = "Room Id cannot be empty")
    private Long roomId;
}
