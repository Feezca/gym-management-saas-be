package com.gym.service.auth;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.request.LoginRequest;
import com.gym.dto.request.RefreshTokenRequest;
import com.gym.dto.response.LoginResponse;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(RefreshTokenRequest request);
    void logout(String refreshToken);
    LoginResponse registerFirstAdmin(CreateUserRequest request);
}
