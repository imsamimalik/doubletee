package com.sda.doubleTee.dto;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedTTDto {
    // make a dto to get a list of subjects from frontend inputs
    List<String> subjects = Arrays.asList(new String[7]);

}
