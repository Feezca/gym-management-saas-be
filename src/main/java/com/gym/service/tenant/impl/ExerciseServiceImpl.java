package com.gym.service.tenant.impl;

import com.gym.domain.public_.entity.ExerciseGlobal;
import com.gym.domain.tenant.entity.Exercise;
import com.gym.dto.request.CreateExerciseRequest;
import com.gym.dto.response.ExerciseResponse;
import com.gym.exception.ResourceNotFoundException;
import com.gym.repository.public_.ExerciseGlobalRepository;
import com.gym.repository.tenant.ExerciseRepository;
import com.gym.service.tenant.ExerciseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseGlobalRepository exerciseGlobalRepository;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository,
                               ExerciseGlobalRepository exerciseGlobalRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseGlobalRepository = exerciseGlobalRepository;
    }

    @Override
    @Transactional
    public ExerciseResponse createExercise(CreateExerciseRequest request) {
        ExerciseGlobal catalogExercise = exerciseGlobalRepository.findById(request.getCatalogExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Catalog exercise not found with id: " + request.getCatalogExerciseId()));

        Exercise exercise = new Exercise();
        exercise.setCatalogExercise(catalogExercise);
        exercise.setCustomName(request.getCustomName());
        exercise.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);

        Exercise savedExercise = exerciseRepository.save(exercise);
        return mapToResponse(savedExercise);
    }

    @Override
    public ExerciseResponse getExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + id));
        return mapToResponse(exercise);
    }

    @Override
    public Page<ExerciseResponse> getAllExercises(Pageable pageable) {
        return exerciseRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(Long id, CreateExerciseRequest request) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + id));

        ExerciseGlobal catalogExercise = exerciseGlobalRepository.findById(request.getCatalogExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Catalog exercise not found with id: " + request.getCatalogExerciseId()));

        exercise.setCatalogExercise(catalogExercise);
        exercise.setCustomName(request.getCustomName());
        exercise.setIsEnabled(request.getIsEnabled() != null ? request.getIsEnabled() : true);

        Exercise updatedExercise = exerciseRepository.save(exercise);
        return mapToResponse(updatedExercise);
    }

    @Override
    @Transactional
    public void deleteExercise(Long id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exercise not found with id: " + id);
        }
        exerciseRepository.deleteById(id);
    }

    private ExerciseResponse mapToResponse(Exercise exercise) {
        ExerciseResponse response = new ExerciseResponse();
        response.setId(exercise.getId());
        ExerciseGlobal catalog = exercise.getCatalogExercise();
        response.setCatalogExerciseId(catalog.getId());
        response.setCatalogExerciseName(catalog.getName());
        response.setMuscleGroup(catalog.getMuscleGroup());
        response.setEquipment(catalog.getEquipment());
        response.setCustomName(exercise.getCustomName());
        response.setIsEnabled(exercise.getIsEnabled());
        return response;
    }
}
