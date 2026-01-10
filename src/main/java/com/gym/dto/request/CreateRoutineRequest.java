package com.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoutineRequest {
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
}
