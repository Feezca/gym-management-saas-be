package com.gym.controller;

import com.gym.dto.request.CreateRoutineRequest;
import com.gym.dto.response.RoutineResponse;
import com.gym.service.RoutineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/trainer")
@PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
public class TrainerController {

    private final RoutineService routineService;

    public TrainerController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/routines")
    public ResponseEntity<RoutineResponse> createRoutine(@Valid @RequestBody CreateRoutineRequest request,
                                                          Authentication authentication) {
        // In a real application, you would extract the trainer ID from the JWT token
        Long trainerId= 1L; // Placeholder
        return ResponseEntity.status(HttpStatus.CREATED).body(routineService.createRoutine(request, trainerId));
    }

    @GetMapping("/routines/member/{memberId}")
    public ResponseEntity<List<RoutineResponse>> getRoutinesByMember(@PathVariable Long memberId) {
        return ResponseEntity.ok(routineService.getRoutinesByMemberId(memberId));
    }

    @GetMapping("/routines/{id}")
    public ResponseEntity<RoutineResponse> getRoutineById(@PathVariable Long id) {
        return ResponseEntity.ok(routineService.getRoutineById(id));
    }

    @DeleteMapping("/routines/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }
}
