package com.gym.service;

import com.gym.dto.request.CreateRoutineRequest;
import com.gym.dto.response.RoutineResponse;

import java.util.List;
import java.util.UUID;

public interface RoutineService {
    RoutineResponse createRoutine(CreateRoutineRequest request, UUID trainerId);
    RoutineResponse getRoutineById(UUID id);
    List<RoutineResponse> getRoutinesByMemberId(UUID memberId);
    void deleteRoutine(UUID id);
}
