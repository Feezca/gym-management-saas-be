package com.gym.service.impl;

import com.gym.dto.request.CreateUserRequest;
import com.gym.dto.response.UserResponse;
import com.gym.entity.User;
import com.gym.enums.Role;
import com.gym.mapper.UserMapper;
import com.gym.repository.UserRepository;
import com.gym.service.SuperAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public SuperAdminServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public UserResponse createAdmin(CreateUserRequest request) {
        User admin = new User();
        admin.setName("Super Admin");
        admin.setEmail(request.getEmail());
        admin.setPassword(encoder.encode(request.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);
        userRepository.save(admin);
        log.debug("First superadmin user created with email: {}", admin.getEmail());

        return UserMapper.userToUserResponse(admin);

    }


}
