package com.gym.service.tenant;

import com.gym.dto.request.CreateExerciseRequest;
import com.gym.dto.response.ExerciseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExerciseService {
    ExerciseResponse createExercise(CreateExerciseRequest request);
    ExerciseResponse getExerciseById(Long id);
    Page<ExerciseResponse> getAllExercises(Pageable pageable);
    ExerciseResponse updateExercise(Long id, CreateExerciseRequest request);
    void deleteExercise(Long id);
}
