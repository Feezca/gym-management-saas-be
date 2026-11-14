package com.gym.service.tenant.impl;

import com.gym.domain.public_.entity.Gym;
import com.gym.domain.public_.entity.User;
import com.gym.domain.tenant.entity.Trainer;
import com.gym.dto.request.CreateTrainerRequest;
import com.gym.dto.response.UserResponse;
import com.gym.enums.PaymentType;
import com.gym.enums.Role;
import com.gym.exception.BadRequestException;
import com.gym.exception.ConflictException;
import com.gym.exception.ResourceNotFoundException;
import com.gym.repository.public_.GymRepository;
import com.gym.repository.public_.UserRepository;
import com.gym.repository.tenant.MemberRepository;
import com.gym.repository.tenant.TrainerRepository;
import com.gym.util.SpecificationService;
import com.gym.util.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    private static final String TENANT = "gym_test";

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private GymRepository gymRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SpecificationService<User> specificationService;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        TenantContext.setTenant(TENANT);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void shouldCreateTrainerAndPublicUserWhenUserDoesNotExist() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");
        Gym gym = gym(10L, TENANT);

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("secret")).thenReturn("encoded-secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(5L);
            return user;
        });
        when(trainerRepository.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = memberService.createTrainer(request);

        assertNotNull(response);
        assertEquals(5L, response.getId());
        assertEquals("trainer@gym.com", response.getEmail());
        assertEquals(Role.TRAINER, response.getRole());
        assertTrue(response.getEnabled());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("Trainer One", savedUser.getName());
        assertEquals("trainer@gym.com", savedUser.getEmail());
        assertEquals("encoded-secret", savedUser.getPassword());
        assertEquals(Role.TRAINER, savedUser.getRole());
        assertEquals(TENANT, savedUser.getTenant());
        assertEquals(10L, savedUser.getGym().getId());
        assertTrue(savedUser.getEnabled());

        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository).save(trainerCaptor.capture());
        Trainer savedTrainer = trainerCaptor.getValue();
        assertEquals("Trainer One", savedTrainer.getName());
        assertEquals("trainer@gym.com", savedTrainer.getEmail());
        assertEquals("555-1234", savedTrainer.getPhone());
        assertEquals(new BigDecimal("1000.00"), savedTrainer.getAgreedAmount());
        assertEquals(PaymentType.MONTHLY, savedTrainer.getPaymentType());
    }

    @Test
    void shouldCreateOnlyTrainerWhenPublicTrainerUserAlreadyExistsForSameGym() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");
        Gym gym = gym(10L, TENANT);
        User existingUser = user(7L, "trainer@gym.com", Role.TRAINER, TENANT, gym);

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        when(trainerRepository.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = memberService.createTrainer(request);

        assertEquals(7L, response.getId());
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void shouldCompleteTenantWhenExistingTrainerUserHasNoTenant() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");
        Gym gym = gym(10L, TENANT);
        User existingUser = user(7L, "trainer@gym.com", Role.TRAINER, null, null);

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(trainerRepository.save(any(Trainer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        memberService.createTrainer(request);

        assertEquals(TENANT, existingUser.getTenant());
        assertEquals(10L, existingUser.getGym().getId());
        verify(userRepository).save(existingUser);
        verify(trainerRepository).save(any(Trainer.class));
    }

    @Test
    void shouldRejectWhenTenantTrainerAlreadyExists() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");
        Gym gym = gym(10L, TENANT);

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> memberService.createTrainer(request));

        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void shouldRejectWhenPublicUserHasDifferentRole() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");
        Gym gym = gym(10L, TENANT);
        User existingUser = user(7L, "trainer@gym.com", Role.ADMIN, TENANT, gym);

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class, () -> memberService.createTrainer(request));

        verify(userRepository, never()).save(any(User.class));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void shouldRejectWhenTrainerUserBelongsToAnotherGym() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");
        Gym gym = gym(10L, TENANT);
        User existingUser = user(7L, "trainer@gym.com", Role.TRAINER, "gym_other", gym(20L, "gym_other"));

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class, () -> memberService.createTrainer(request));

        verify(userRepository, never()).save(any(User.class));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void shouldRequirePasswordWhenPublicUserDoesNotExist() {
        CreateTrainerRequest request = request("trainer@gym.com", null, "Trainer One");
        Gym gym = gym(10L, TENANT);

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.of(gym));
        when(trainerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> memberService.createTrainer(request));

        verify(userRepository, never()).save(any(User.class));
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    @Test
    void shouldThrowWhenGymIsNotFoundForTenant() {
        CreateTrainerRequest request = request("trainer@gym.com", "secret", "Trainer One");

        when(gymRepository.findBySchemaName(TENANT)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.createTrainer(request));

        verify(userRepository, never()).findByEmail(anyString());
        verify(trainerRepository, never()).save(any(Trainer.class));
    }

    private CreateTrainerRequest request(String email, String password, String name) {
        CreateTrainerRequest request = new CreateTrainerRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setName(name);
        request.setPhone("555-1234");
        request.setAgreedAmount(new BigDecimal("1000.00"));
        request.setPaymentType(PaymentType.MONTHLY);
        return request;
    }

    private Gym gym(Long id, String schemaName) {
        Gym gym = new Gym();
        gym.setId(id);
        gym.setSchemaName(schemaName);
        return gym;
    }

    private User user(Long id, String email, Role role, String tenant, Gym gym) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setRole(role);
        user.setTenant(tenant);
        user.setGym(gym);
        user.setEnabled(true);
        return user;
    }
}
