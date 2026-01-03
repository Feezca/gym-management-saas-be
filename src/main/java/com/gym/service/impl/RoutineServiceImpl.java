package com.gym.service.impl;

import com.gym.dto.request.CreateRoutineRequest;
import com.gym.dto.response.RoutineResponse;
import com.gym.entity.Routine;
import com.gym.exception.ResourceNotFoundException;
import com.gym.repository.RoutineRepository;
import com.gym.service.RoutineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineServiceImpl(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    @Override
    @Transactional
    public RoutineResponse createRoutine(CreateRoutineRequest request, UUID trainerId) {
        Routine routine = new Routine();
        routine.setMemberId(request.getMemberId());
        routine.setTrainerId(trainerId);
        routine.setName(request.getName());
        routine.setDescription(request.getDescription());

        Routine savedRoutine = routineRepository.save(routine);
        return mapToResponse(savedRoutine);
    }

    @Override
    public RoutineResponse getRoutineById(UUID id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found with id: " + id));
        return mapToResponse(routine);
    }

    @Override
    public List<RoutineResponse> getRoutinesByMemberId(UUID memberId) {
        return routineRepository.findByMemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRoutine(UUID id) {
        if (!routineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Routine not found with id: " + id);
        }
        routineRepository.deleteById(id);
    }

    private RoutineResponse mapToResponse(Routine routine) {
        return new RoutineResponse(
                routine.getId(),
                routine.getMemberId(),
                routine.getTrainerId(),
                routine.getName(),
                routine.getDescription(),
                routine.getCreatedAt()
        );
    }
}
