package com.gym.service.provisioning;

import com.gym.domain.public_.entity.Gym;
import com.gym.repository.public_.GymRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantMigrationRunnerTest {

    @Mock
    private GymRepository gymRepository;

    @Mock
    private TenantProvisioningService tenantProvisioningService;

    @InjectMocks
    private TenantMigrationRunner runner;

    @Test
    void shouldMigrateEveryTenantSchema() {
        when(gymRepository.findAll()).thenReturn(List.of(gym("gym_alpha"), gym("gym_beta")));

        runner.run(new DefaultApplicationArguments(new String[0]));

        verify(tenantProvisioningService).createTenant("gym_alpha");
        verify(tenantProvisioningService).createTenant("gym_beta");
    }

    @Test
    void shouldContinueWithOtherTenantsWhenOneMigrationFails() {
        when(gymRepository.findAll()).thenReturn(List.of(gym("gym_alpha"), gym("gym_beta")));
        doThrow(new RuntimeException("migration failed"))
                .when(tenantProvisioningService).createTenant("gym_alpha");

        runner.run(new DefaultApplicationArguments(new String[0]));

        verify(tenantProvisioningService).createTenant("gym_alpha");
        verify(tenantProvisioningService).createTenant("gym_beta");
    }

    private Gym gym(String schemaName) {
        Gym gym = new Gym();
        gym.setSchemaName(schemaName);
        return gym;
    }
}
