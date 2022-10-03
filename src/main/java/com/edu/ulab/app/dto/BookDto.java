package com.edu.ulab.app.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@NotNull
public class BookDto {
    @NotNull
    private Long id;
    private Long userId;
    private String title;
    private String author;
    private Integer pageCount;
}
