package com.example.homework.repository;

import com.example.homework.model.NotificationEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotificationEventRepository {
    private final Connection connection;

    public NotificationEventRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(Long userId, String type, String payloadJson) throws SQLException {
        String sql = """
                INSERT INTO notification_events(user_id, event_type, payload_json)
                VALUES (?, ?, CAST(? AS jsonb))
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, type);
            statement.setString(3, payloadJson);
            statement.executeUpdate();
        }
    }

    public List<NotificationEvent> findAll() throws SQLException {
        String sql = """
                SELECT id, user_id, event_type, payload_json::text, created_at
                FROM notification_events
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<NotificationEvent> events = new ArrayList<>();
            while (rs.next()) {
                events.add(new NotificationEvent(
                        rs.getLong("id"),
                        rs.getLong("user_id"),
                        rs.getString("event_type"),
                        rs.getString("payload_json"),
                        rs.getObject("created_at", OffsetDateTime.class)
                ));
            }
            return events;
        }
    }

    public long count() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM notification_events");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }
}
