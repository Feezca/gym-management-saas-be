package com.gym.service.auth.impl;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.request.LoginRequest;
import com.gym.dto.request.RefreshTokenRequest;
import com.gym.dto.response.CurrentUserResponse;
import com.gym.dto.response.LoginResponse;
import com.gym.dto.response.UserResponse;
import com.gym.domain.public_.entity.RefreshToken;
import com.gym.domain.public_.entity.User;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.exception.UnauthorizedException;
import com.gym.mapper.UserMapper;
import com.gym.repository.public_.RefreshTokenRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.security.JwtUtil;
import com.gym.security.UserPrincipal;
import com.gym.service.auth.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository,
                           RefreshTokenRepository refreshTokenRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));
        log.debug("User {} found for login", user.getEmail());
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        if (!user.getEnabled()) {
            throw new UnauthorizedException("Account is disabled");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name(), user.getTenant());
        String refreshTokenString = jwtUtil.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        CurrentUserResponse userResponse = UserMapper.userToCurrentUserResponse(user);
        return new LoginResponse(accessToken, refreshTokenString, userResponse);
    }

    @Override
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (refreshToken.getRevoked()) {
            throw new UnauthorizedException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new UnauthorizedException("Refresh token has expired");
        }

        User user = refreshToken.getUser();
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name(), user.getTenant());

        CurrentUserResponse userResponse = UserMapper.userToCurrentUserResponse(user);
        return new LoginResponse(accessToken, request.getRefreshToken(), userResponse);
    }

    @Override
    public LoginResponse registerFirstAdmin(CreateUserRequest request) {
        /* long adminCount = userRepository.countByRole("ADMIN");
        if (adminCount > 0) {
            throw new UnauthorizedException("Admin user already exists");
        } */

        User admin = new User();
        admin.setName("Super Admin");
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Role.SUPERADMIN);
        admin.setEnabled(true);
        userRepository.save(admin);
        log.debug("First superadmin user created with email: {}", admin.getEmail());

        String accessToken = jwtUtil.generateAccessToken(admin.getEmail(), admin.getRole().name(), "public");
        String refreshTokenString = jwtUtil.generateRefreshToken(admin.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setUser(admin);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        CurrentUserResponse userResponse = UserMapper.userToCurrentUserResponse(admin);
        return new LoginResponse(accessToken, refreshTokenString, userResponse);
    }

    @Override
    public void logout(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
    public CurrentUserResponse getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        var principal = (UserPrincipal) authentication.getPrincipal();

        return new CurrentUserResponse(
                principal.getId(),
                principal.getEmail(),
                principal.getRole()
        );
    }

}
