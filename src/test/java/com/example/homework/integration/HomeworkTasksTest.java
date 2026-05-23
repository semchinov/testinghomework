package com.example.homework.integration;

import com.example.homework.fixtures.UserFixture;
import com.example.homework.model.NotificationEvent;
import com.example.homework.model.User;
import com.example.homework.repository.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HomeworkTasksTest extends AbstractPostgresIntegrationTest {

    @Test
    @Tag("smoke")
    void shouldFindUserByEmailAfterSavingThroughRepository() throws Exception {
        // arrange
        User user = userRepository.save(UserFixture.defaultActiveUser());

        // act
        User savedUser = userRepository.findByEmail(user.getEmail()).orElseThrow();

        // assert
        assertEquals("alice@example.com", savedUser.getEmail());
        assertEquals("Alice Johnson", savedUser.getFullName());
        assertEquals("ACTIVE", savedUser.getStatus());
    }

    @Test
    @Tag("regression")
    void shouldNotReturnInactiveUserInFindActiveUsers() throws Exception {
        // arrange
        userRepository.save(UserFixture.defaultActiveUser());
        userRepository.save(UserFixture.inactiveUser());

        // act
        List<User> activeUsers = userRepository.findActiveUsers();

        // assert
        assertEquals(1, activeUsers.size());
        assertEquals("alice@example.com", activeUsers.get(0).getEmail());
    }

    @Test
    @Tag("smoke")
    void shouldCreateNotificationEventOnRegistration() throws Exception {
        // arrange + act
        User user = userRegistrationService.register("carol@example.com", "Carol Smith");

        // assert
        List<NotificationEvent> events = notificationEventRepository.findAll();
        assertEquals(1, userRepository.count());
        assertEquals(1, notificationEventRepository.count());
        assertEquals("USER_REGISTERED", events.get(0).getType());
        assertTrue(events.get(0).getPayloadJson().contains(user.getEmail()));
    }

    @Test
    @Tag("regression")
    void shouldRollbackChangesBetweenTests() throws Exception {
        userRepository.save(UserFixture.defaultActiveUser());

        assertEquals(1, userRepository.count());
    }

    @Test
    @Tag("regression")
    void shouldStartFromCleanStateInAnotherTest() throws Exception {
        assertEquals(0, userRepository.count());
        assertEquals(0, notificationEventRepository.count());
    }

    @Test
    @Tag("e2e")
    void shouldDemonstrateThatCommitInsideCodeBreaksRollbackIsolation() throws Exception {
        userRegistrationService.registerAndCommitInsideService("eve@example.com", "Eve Green", connection);
        connection.rollback();

        try (Connection newConnection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(),
                POSTGRES.getUsername(),
                POSTGRES.getPassword()
        )) {
            UserRepository newUserRepository = new UserRepository(newConnection);
            assertEquals(1, newUserRepository.count());
            newConnection.createStatement().executeUpdate("DELETE FROM users");
        }
    }
}
