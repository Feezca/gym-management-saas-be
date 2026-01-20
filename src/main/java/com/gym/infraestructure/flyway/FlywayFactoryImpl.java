package com.gym.infraestructure.flyway;

import org.flywaydb.core.Flyway;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FlywayFactoryImpl implements IFlywayFactory {

    private final DataSource dataSource;

    public FlywayFactoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Flyway create(String schema) {
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:db/migration/tenants")
                .createSchemas(true)
                .baselineOnMigrate(true)
                .load();
    }
}
