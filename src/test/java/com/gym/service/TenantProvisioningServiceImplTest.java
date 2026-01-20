package com.gym.service;

import com.gym.infraestructure.flyway.FlywayFactoryImpl;
import com.gym.service.provisioning.impl.TenantProvisioningServiceImpl;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TenantProvisioningServiceImplTest {

    @Mock
    FlywayFactoryImpl flywayFactory;

    @Mock
    Flyway flyway;

    @InjectMocks
    TenantProvisioningServiceImpl service;

    @Test
    void shouldRunFlywayMigrationForTenant() {

        String schema = "gym_test";
        when(flywayFactory.create(schema)).thenReturn(flyway);

        service.createTenant(schema);

        verify(flywayFactory).create(schema);
        verify(flyway).migrate();
    }
}

