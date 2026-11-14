package com.gym.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateExerciseRequest {

    @NotNull(message = "Catalog exercise ID is required")
    private Long catalogExerciseId;

    private String customName;

    @NotNull(message = "Is enabled is required")
    private Boolean isEnabled = true;
}
