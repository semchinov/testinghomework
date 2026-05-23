package com.example.homework.integration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionIsolationIntegrationTest extends AbstractPostgresIntegrationTest {

    @Test
    void shouldStartWithEmptyDatabaseInEachTest() throws Exception {
        assertEquals(0, userRepository.count());

        userRegistrationService.register("eva@example.com", "Eva Green");

        assertEquals(1, userRepository.count());
    }

    @Test
    void shouldAlsoStartWithEmptyDatabaseInAnotherTest() throws Exception {
        assertEquals(0, userRepository.count());
        assertEquals(0, notificationEventRepository.count());
    }
}
