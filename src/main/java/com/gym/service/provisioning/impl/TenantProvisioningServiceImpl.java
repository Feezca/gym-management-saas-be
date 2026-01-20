package com.gym.service.provisioning.impl;

import com.gym.infraestructure.flyway.FlywayFactoryImpl;
import com.gym.service.provisioning.TenantProvisioningService;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TenantProvisioningServiceImpl implements TenantProvisioningService {

    private final FlywayFactoryImpl flywayFactory;

    public TenantProvisioningServiceImpl( FlywayFactoryImpl flywayFactory) {
        this.flywayFactory = flywayFactory;
    }

    @Override
    public void createTenant(String schema) {
        Flyway flyway = flywayFactory.create(schema);
        flyway.migrate();
    }
}

