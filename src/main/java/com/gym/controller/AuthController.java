package com.gym.controller;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.request.LoginRequest;
import com.gym.dto.request.RefreshTokenRequest;
import com.gym.dto.response.CurrentUserResponse;
import com.gym.dto.response.LoginResponse;
import com.gym.dto.response.UserResponse;
import com.gym.security.JwtUtil;
import com.gym.security.UserPrincipal;
import com.gym.service.auth.IAuthService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthController {

    private final IAuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    public AuthController(IAuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
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
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        CurrentUserResponse response = new CurrentUserResponse(
                principal.getId(),
                principal.getUsername(),
                principal.getRole()
        );

        return ResponseEntity.ok(response);
    }


}
