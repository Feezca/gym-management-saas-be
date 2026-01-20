package com.gym.service.gym.impl;

import com.gym.dto.request.CreateGymRequest;
import com.gym.dto.response.CreateGymResponse;
import com.gym.domain.public_.entity.Gym;
import com.gym.domain.public_.entity.User;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.exception.UnauthorizedException;
import com.gym.repository.public_.GymRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.service.gym.GymService;
import com.gym.service.provisioning.TenantProvisioningService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class GymServiceImpl implements GymService {

    private final GymRepository repository;
    private final UserRepository userRepository;
    private final TenantProvisioningService tenantProvisioningService;
    public GymServiceImpl(GymRepository repository, UserRepository userRepository, TenantProvisioningService tenantProvisioningService) {

        this.repository = repository;
        this.userRepository = userRepository;
        this.tenantProvisioningService = tenantProvisioningService;
    }

    @Transactional
    @Override
    public CreateGymResponse createGym(CreateGymRequest request) {

        User owner = userRepository.findByIdAndRole(request.getAdminUserId(), Role.ADMIN).orElseThrow(()-> new ResourceNotFoundException("Admin user not found with id: " + request.getAdminUserId()));

        String normalizedGymName = request.getName()
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "_");

        String schema = "gym_" + normalizedGymName;

        if (repository.findBySchemaName(schema).isPresent()) {
            throw new UnauthorizedException("Gym schema already exists");
        }

        // 🔥 1. Crear tenant real
        tenantProvisioningService.createTenant(schema);
        // 🔥 2. Crear gym en la base de datos public
        Gym gym = new Gym();
        gym.setName(request.getName());
        gym.setSchemaName(schema);
        gym.setOwner(owner);
        gym.setCreatedAt(LocalDateTime.now());

        Gym savedGym = repository.save(gym);

        // 🔥 3. Actualizar usuario admin para asignarle el gym y el tenant
        owner.setTenant(schema);
        userRepository.save(owner);


        CreateGymResponse response = new CreateGymResponse();
        response.setId(savedGym.getId());
        response.setName(savedGym.getName());
        response.setAdminUserId(savedGym.getOwner().getId());

        return response;
    }

}
