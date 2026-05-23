package com.example.homework.repository;

import com.example.homework.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public User save(User user) throws SQLException {
        String sql = """
                INSERT INTO users(email, full_name, status)
                VALUES (?, ?, ?)
                RETURNING id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getStatus());

            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                user.setId(rs.getLong("id"));
                return user;
            }
        }
    }

    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = """
                SELECT id, email, full_name, status
                FROM users
                WHERE email = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(map(rs));
            }
        }
    }

    public List<User> findActiveUsers() throws SQLException {
        String sql = """
                SELECT id, email, full_name, status
                FROM users
                WHERE status = 'ACTIVE'
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                users.add(map(rs));
            }
            return users;
        }
    }

    public long count() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users");
             ResultSet rs = statement.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("email"),
                rs.getString("full_name"),
                rs.getString("status")
        );
    }
}
