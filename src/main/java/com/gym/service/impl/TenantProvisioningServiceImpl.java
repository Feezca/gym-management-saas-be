package com.gym.service.impl;

import com.gym.service.TenantProvisioningService;
import org.flywaydb.core.Flyway;
import org.hibernate.service.spi.InjectService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class TenantProvisioningServiceImpl implements TenantProvisioningService {

    private final JdbcTemplate jdbcTemplate;

    private Flyway flyway;

    public TenantProvisioningServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTenant(String schema) {

        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schema);

        Flyway flyway = Flyway.configure()
                .dataSource(jdbcTemplate.getDataSource())
                .schemas(schema)
                .locations("classpath:db/migration/tenants")
                .load();

        flyway.migrate();
    }
}

