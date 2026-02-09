package com.gym.infraestructure.flyway;

import com.gym.exception.GlobalExceptionHandler;
import com.gym.exception.UnauthorizedException;
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
        if ("public".equals(schema)) {
            throw new UnauthorizedException("Cannot create Flyway instance for 'public' schema");
        }
        return Flyway.configure()
                .dataSource(dataSource)
                .schemas(schema)
                .defaultSchema(schema)
                .locations("classpath:db/migration/tenant")
                .createSchemas(true)
                .baselineOnMigrate(true)
                .load();
    }
}
