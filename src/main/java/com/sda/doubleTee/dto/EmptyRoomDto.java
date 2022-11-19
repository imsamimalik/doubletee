package com.sda.doubleTee.dto;

import com.sda.doubleTee.constants.Days;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

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
