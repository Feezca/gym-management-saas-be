package com.gym.controller;

import com.gym.dto.response.RoutineResponse;
import com.gym.service.RoutineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/member")
@PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
public class MemberController {

    private final RoutineService routineService;

    public MemberController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping("/routines")
    public ResponseEntity<List<RoutineResponse>> getMyRoutines(Authentication authentication) {
        // In a real application, you would extract the member ID from the JWT token
        Long memberId = 1L;  // Placeholder
        return ResponseEntity.ok(routineService.getRoutinesByMemberId(memberId));
    }

    @GetMapping("/routines/{id}")
    public ResponseEntity<RoutineResponse> getRoutineById(@PathVariable Long id) {
        return ResponseEntity.ok(routineService.getRoutineById(id));
    }
}
