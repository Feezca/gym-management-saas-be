package com.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineResponse {
    private UUID id;
    private UUID memberId;
    private UUID trainerId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
