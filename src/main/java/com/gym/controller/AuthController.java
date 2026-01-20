package com.gym.controller;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.request.LoginRequest;
import com.gym.dto.request.RefreshTokenRequest;
import com.gym.dto.response.LoginResponse;
import com.gym.service.auth.IAuthService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/test")
    public String test() {
        return "Auth Service is running.";
    }

    @PostMapping("/register-first-admin")
    public ResponseEntity<LoginResponse> registerFirstAdmin(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(authService.registerFirstAdmin(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }
}
