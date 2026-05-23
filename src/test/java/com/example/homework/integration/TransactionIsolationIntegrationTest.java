package com.example.homework.integration;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionIsolationIntegrationTest extends AbstractPostgresIntegrationTest {

    @Test
    @Tag("regression")
    void shouldStartWithEmptyDatabaseInEachTest() throws Exception {
        assertEquals(0, userRepository.count());

        userRegistrationService.register("eva@example.com", "Eva Green");

        assertEquals(1, userRepository.count());
    }

    @Test
    @Tag("regression")
    void shouldAlsoStartWithEmptyDatabaseInAnotherTest() throws Exception {
        assertEquals(0, userRepository.count());
        assertEquals(0, notificationEventRepository.count());
    }
}
