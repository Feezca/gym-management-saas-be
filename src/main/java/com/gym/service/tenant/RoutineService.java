package com.gym.service.tenant;

import com.gym.dto.request.CreateRoutineRequest;
import com.gym.dto.response.RoutineResponse;

import java.util.List;

public interface RoutineService {
    RoutineResponse createRoutine(CreateRoutineRequest request, Long trainerId);
    RoutineResponse getRoutineById(Long id);
    List<RoutineResponse> getRoutinesByMemberId(Long memberId);
    void deleteRoutine(Long id);
}
