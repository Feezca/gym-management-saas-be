package com.gym.dto.response;

import com.gym.enums.Equipment;
import com.gym.enums.MuscleGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponse {
    private Long id;
    private Long catalogExerciseId;
    private String catalogExerciseName;
    private MuscleGroup muscleGroup;
    private Equipment equipment;
    private String customName;
    private Boolean isEnabled;
}
