package com.gym.service.tenant.impl;

import com.gym.dto.request.CreateRoutineRequest;
import com.gym.dto.response.RoutineResponse;
import com.gym.domain.tenant.entity.Routine;
import com.gym.exception.ResourceNotFoundException;
import com.gym.repository.tenant.RoutineRepository;
import com.gym.service.tenant.RoutineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;

    public RoutineServiceImpl(RoutineRepository routineRepository) {
        this.routineRepository = routineRepository;
    }

    @Override
    @Transactional
    public RoutineResponse createRoutine(CreateRoutineRequest request, Long trainerId) {
        Routine routine = new Routine();
        //routine.setMember(request.getMemberId());
        //routine.setTrainer(trainerId);
        routine.setName(request.getName());
        routine.setDescription(request.getDescription());

        Routine savedRoutine = routineRepository.save(routine);
        return mapToResponse(savedRoutine);
    }

    @Override
    public RoutineResponse getRoutineById(Long id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Routine not found with id: " + id));
        return mapToResponse(routine);
    }

    @Override
    public List<RoutineResponse> getRoutinesByMemberId(Long memberId) {
        return routineRepository.findByMemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRoutine(Long id) {
        if (!routineRepository.existsById(id)) {
            throw new ResourceNotFoundException("Routine not found with id: " + id);
        }
        routineRepository.deleteById(id);
    }

    private RoutineResponse mapToResponse(Routine routine) {
        RoutineResponse res = new RoutineResponse();
        res.setId(routine.getId());
        res.setMemberId(routine.getMember().getId());
        res.setTrainerId(routine.getTrainer().getId());
        res.setName(routine.getName());
        res.setDescription(routine.getDescription());
        res.setCreatedAt(routine.getCreatedAt());
        return res;
    }
}
