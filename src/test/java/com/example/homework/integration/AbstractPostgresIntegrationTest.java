package com.example.homework.integration;

import com.example.homework.db.FlywayMigrator;
import com.example.homework.repository.NotificationEventRepository;
import com.example.homework.repository.UserRepository;
import com.example.homework.service.UserRegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;

@Testcontainers
public abstract class AbstractPostgresIntegrationTest {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("demo_db")
            .withUsername("demo_user")
            .withPassword("demo_pass");

    protected Connection connection;
    protected UserRepository userRepository;
    protected NotificationEventRepository notificationEventRepository;
    protected UserRegistrationService userRegistrationService;

    @BeforeAll
    static void migrateSchema() {
        FlywayMigrator.migrate(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
    }

    @BeforeEach
    void openConnectionAndStartTransaction() throws Exception {
        connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        );
        connection.setAutoCommit(false);

        userRepository = new UserRepository(connection);
        notificationEventRepository = new NotificationEventRepository(connection);
        userRegistrationService = new UserRegistrationService(
                userRepository,
                notificationEventRepository,
                new ObjectMapper()
        );
    }

    @AfterEach
    void rollbackAndCloseConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.rollback();
            connection.close();
        }
    }
}

