package com.gym.service;

import com.gym.domain.public_.entity.Gym;
import com.gym.domain.public_.entity.User;
import com.gym.dto.request.CreateGymRequest;
import com.gym.dto.response.CreateGymResponse;
import com.gym.enums.Role;
import com.gym.exception.ResourceNotFoundException;
import com.gym.repository.public_.GymRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.service.gym.impl.GymServiceImpl;
import com.gym.service.provisioning.TenantProvisioningService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GymServiceTest {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TenantProvisioningService tenantProvisioningService;

    @InjectMocks
    private GymServiceImpl gymService;

    @Test
    void shouldCreateGymAndAssignTenantToAdmin() {

        // Arrange
        CreateGymRequest request = new CreateGymRequest();
        request.setName("WOD Gym");
        request.setAdminUserId(1L);

        User admin = new User();
        admin.setId(1L);
        admin.setEmail("admin@gym.com");
        admin.setRole(Role.ADMIN);

        when(userRepository.findByIdAndRole(1L, Role.ADMIN))
                .thenReturn(Optional.of(admin));

        when(gymRepository.findBySchemaName("gym_wod_gym"))
                .thenReturn(Optional.empty());

        when(gymRepository.save(any(Gym.class)))
                .thenAnswer(invocation -> {
                    Gym gym = invocation.getArgument(0);
                    gym.setId(10L);
                    gym.setCreatedAt(LocalDateTime.now());
                    return gym;
                });

        // Act
        CreateGymResponse response = gymService.createGym(request);

        // Assert
        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("WOD Gym", response.getName());
        assertEquals(1L, response.getAdminUserId());

        verify(tenantProvisioningService).createTenant("gym_wod_gym");
        verify(userRepository).save(admin);
        verify(gymRepository).save(any(Gym.class));
    }

    @Test
    void shouldThrowExceptionIfAdminDoesNotExist() {

        CreateGymRequest request = new CreateGymRequest();
        request.setName("WOD Gym");
        request.setAdminUserId(99L);

        when(userRepository.findByIdAndRole(99L, Role.ADMIN))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> gymService.createGym(request));

        verifyNoInteractions(tenantProvisioningService);
    }
}
