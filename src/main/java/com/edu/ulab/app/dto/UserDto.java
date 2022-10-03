package com.edu.ulab.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull
public class UserDto {
    @NotNull
    private Long id;
    private String fullName;
    private String title;
    private Integer age;
}
