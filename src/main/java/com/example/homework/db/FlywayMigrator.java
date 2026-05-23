package com.example.homework.db;

import org.flywaydb.core.Flyway;

public final class FlywayMigrator {
    private FlywayMigrator() {
    }

    public static void migrate(String jdbcUrl, String username, String password) {
        Flyway.configure()
                .dataSource(jdbcUrl, username, password)
                .load()
                .migrate();
    }
}
