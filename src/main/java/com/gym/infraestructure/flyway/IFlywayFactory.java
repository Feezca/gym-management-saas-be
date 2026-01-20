package com.gym.infraestructure.flyway;

import org.flywaydb.core.Flyway;

public interface IFlywayFactory {
    Flyway create(String schema);
}
