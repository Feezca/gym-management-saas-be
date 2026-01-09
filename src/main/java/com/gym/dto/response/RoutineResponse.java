package com.gym.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

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
