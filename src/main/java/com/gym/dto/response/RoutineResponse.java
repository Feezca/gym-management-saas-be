package com.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineResponse {
    private Long id;
    private Long memberId;
    private Long trainerId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
