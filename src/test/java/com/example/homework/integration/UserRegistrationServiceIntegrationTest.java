package com.example.homework.integration;

import com.example.homework.model.NotificationEvent;
import com.example.homework.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRegistrationServiceIntegrationTest extends AbstractPostgresIntegrationTest {

    @Test
    void shouldRegisterUserAndCreateNotificationEvent() throws Exception {
        User user = userRegistrationService.register("carol@example.com", "Carol Smith");

        List<NotificationEvent> events = notificationEventRepository.findAll();

        assertEquals(1, userRepository.count());
        assertEquals(1, notificationEventRepository.count());
        assertEquals(user.getId(), events.get(0).getUserId());
        assertEquals("USER_REGISTERED", events.get(0).getType());
        assertTrue(events.get(0).getPayloadJson().contains("carol@example.com"));
        assertTrue(events.get(0).getPayloadJson().contains("Carol Smith"));
    }

    @Test
    void shouldSeeChangesInsideTransaction() throws Exception {
        userRegistrationService.register("dave@example.com", "Dave White");

        assertEquals(1, userRepository.count());
        assertEquals(1, userRegistrationService.activeUserCount());
    }
}
