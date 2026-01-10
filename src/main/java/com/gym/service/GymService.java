package com.gym.service;

import com.gym.dto.request.CreateGymRequest;
import com.gym.dto.response.CreateGymResponse;

public interface GymService {
    CreateGymResponse createGym(CreateGymRequest request);
}
