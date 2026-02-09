package com.gym.dto.request;


import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseFilterDto {
    private Long id;

    private String keyword;

    Boolean active = null;

    @PositiveOrZero
    private Integer size;

    @PositiveOrZero
    private Integer page;
}
