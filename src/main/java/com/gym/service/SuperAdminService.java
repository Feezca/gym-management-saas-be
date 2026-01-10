package com.gym.service;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.UserResponse;

public interface SuperAdminService {
    UserResponse createAdmin(CreateUserRequest request);
}
