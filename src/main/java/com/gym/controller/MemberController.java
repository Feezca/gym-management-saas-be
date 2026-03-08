package com.gym.controller;

import com.gym.dto.request.BaseFilterDto;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.response.MemberResponse;
import com.gym.dto.response.MemberStatisticsResponse;
import com.gym.dto.response.RoutineResponse;
import com.gym.service.tenant.MemberService;
import com.gym.service.tenant.RoutineService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member")
@PreAuthorize("hasAnyRole('ADMIN', 'TRAINER', 'MEMBER')")
public class MemberController {

    private final RoutineService routineService;
    private final MemberService memberService;
    public MemberController(RoutineService routineService, MemberService memberService) {
        this.routineService = routineService;
        this.memberService = memberService;
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

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(@Valid @RequestBody CreateMemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(request));
    }

    @PostMapping
    public MemberResponse createMember(@Valid @RequestBody CreateMemberRequest request) {
        return memberService.createMember(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<MemberResponse> search(@ModelAttribute BaseFilterDto filter) {
        return memberService.search(filter);
    }

    @GetMapping("/statistics")
    public MemberStatisticsResponse getMemberStatistics() {
        return memberService.getMemberStatistics();
    }

    @GetMapping("/recent")
    public List<MemberResponse> getRecentMembers() {
        return memberService.lastFourMembers();
    }

    @GetMapping
    public List<MemberResponse> getAllMembers() {
        return memberService.getAllMembers();
    }
}
