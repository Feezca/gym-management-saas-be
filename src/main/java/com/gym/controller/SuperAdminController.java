package com.gym.controller;

import com.gym.dto.request.CreateGymRequest;
import com.gym.dto.request.CreateMemberRequest;
import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.*;
import com.gym.service.gym.GymService;
import com.gym.service.tenant.MemberService;
import com.gym.service.tenant.ProductService;
import com.gym.service.SuperAdminService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPERADMIN')")
public class SuperAdminController {

    private final MemberService memberService;
    private final ProductService productService;
    private final SuperAdminService service;
    private final GymService gymService;

    public SuperAdminController(MemberService memberService, ProductService productService, SuperAdminService service
            , GymService gymService) {
        this.memberService = memberService;
        this.productService = productService;
        this.service = service;
        this.gymService = gymService;
    }

    // Member Management
    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody CreateMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.createMember(request));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResponse> registerAdmin(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(service.createAdmin(request));
    }

    @PostMapping("/register-gym")
    public ResponseEntity<CreateGymResponse> createGym(@Valid @RequestBody CreateGymRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(gymService.createGym(request));
    }

}
