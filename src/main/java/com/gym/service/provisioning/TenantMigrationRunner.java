package com.gym.service.provisioning;

import com.gym.domain.public_.entity.Gym;
import com.gym.repository.public_.GymRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TenantMigrationRunner implements ApplicationRunner {

    private final GymRepository gymRepository;
    private final TenantProvisioningService tenantProvisioningService;

    public TenantMigrationRunner(GymRepository gymRepository,
                                 TenantProvisioningService tenantProvisioningService) {
        this.gymRepository = gymRepository;
        this.tenantProvisioningService = tenantProvisioningService;
    }

    @Override
    public void run(ApplicationArguments args) {
        List<Gym> gyms = gymRepository.findAll();
        log.info("Applying tenant migrations to {} gym schema(s)", gyms.size());

        gyms.forEach(gym -> migrateTenant(gym.getSchemaName()));
    }

    private void migrateTenant(String schemaName) {
        try {
            tenantProvisioningService.createTenant(schemaName);
            log.info("Tenant migrations applied for schema {}", schemaName);
        } catch (Exception e) {
            log.error("Failed to apply tenant migrations for schema {}", schemaName, e);
        }
    }
}
