package com.gym.service.impl;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.request.LoginRequest;
import com.gym.dto.request.RefreshTokenRequest;
import com.gym.dto.response.LoginResponse;
import com.gym.dto.response.UserResponse;
import com.gym.entity.RefreshToken;
import com.gym.entity.User;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.exception.UnauthorizedException;
import com.gym.mapper.UserMapper;
import com.gym.repository.RefreshTokenRepository;
import com.gym.repository.UserRepository;
import com.gym.security.JwtUtil;
import com.gym.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

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
    @Transactional
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

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
        String refreshTokenString = jwtUtil.generateRefreshToken(user.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        UserResponse userResponse = UserMapper.userToUserResponse(user);
        return new LoginResponse(accessToken, refreshTokenString, userResponse);
    }

    @Override
    @Transactional
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
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());

        UserResponse userResponse = UserMapper.userToUserResponse(user);
        return new LoginResponse(accessToken, request.getRefreshToken(), userResponse);
    }

    @Override
    @Transactional
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

        String accessToken = jwtUtil.generateAccessToken(admin.getEmail(), admin.getRole().name());
        String refreshTokenString = jwtUtil.generateRefreshToken(admin.getEmail());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(refreshTokenString);
        refreshToken.setUser(admin);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        UserResponse userResponse = UserMapper.userToUserResponse(admin);
        return new LoginResponse(accessToken, refreshTokenString, userResponse);
    }

    @Override
    @Transactional
    public void logout(String refreshTokenString) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }


}
