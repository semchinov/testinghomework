package com.example.homework.integration;

import com.example.homework.fixtures.UserFixture;
import com.example.homework.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRepositoryIntegrationTest extends AbstractPostgresIntegrationTest {

    @Test
    void shouldSaveAndFindUserByEmail() throws Exception {
        User user = userRepository.save(UserFixture.defaultActiveUser());

        User savedUser = userRepository.findByEmail("alice@example.com").orElseThrow();

        assertNotNull(user.getId());
        assertEquals("alice@example.com", savedUser.getEmail());
        assertEquals("Alice Johnson", savedUser.getFullName());
        assertEquals("ACTIVE", savedUser.getStatus());
    }

    @Test
    void shouldReturnOnlyActiveUsers() throws Exception {
        userRepository.save(UserFixture.defaultActiveUser());
        userRepository.save(UserFixture.inactiveUser());

        List<User> activeUsers = userRepository.findActiveUsers();

        assertEquals(1, activeUsers.size());
        assertEquals("alice@example.com", activeUsers.get(0).getEmail());
    }

    @Test
    void shouldFailOnDuplicateEmailBecauseRealConstraintExists() throws Exception {
        userRepository.save(UserFixture.defaultActiveUser());

        SQLException exception = assertThrows(SQLException.class,
                () -> userRepository.save(new User("alice@example.com", "Another Alice", "ACTIVE")));

        assertTrue(exception.getMessage().toLowerCase().contains("duplicate")
                || "23505".equals(exception.getSQLState()));
    }
}
