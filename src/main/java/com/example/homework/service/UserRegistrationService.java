package com.example.homework.service;

import com.example.homework.model.User;
import com.example.homework.repository.NotificationEventRepository;
import com.example.homework.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class UserRegistrationService {
    private final UserRepository userRepository;
    private final NotificationEventRepository notificationEventRepository;
    private final ObjectMapper objectMapper;

    public UserRegistrationService(UserRepository userRepository,
                                   NotificationEventRepository notificationEventRepository,
                                   ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.notificationEventRepository = notificationEventRepository;
        this.objectMapper = objectMapper;
    }

    public User register(String email, String fullName) throws Exception {
        User user = userRepository.save(new User(email, fullName, "ACTIVE"));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", user.getEmail());
        payload.put("fullName", user.getFullName());
        payload.put("status", user.getStatus());

        notificationEventRepository.save(
                user.getId(),
                "USER_REGISTERED",
                objectMapper.writeValueAsString(payload)
        );

        return user;
    }

    public void registerAndCommitInsideService(String email, String fullName, java.sql.Connection connection) throws Exception {
        userRepository.save(new User(email, fullName, "ACTIVE"));
        connection.commit();
    }

    public long activeUserCount() throws SQLException {
        return userRepository.findActiveUsers().size();
    }
}
