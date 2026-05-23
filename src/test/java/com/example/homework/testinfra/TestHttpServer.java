package com.example.homework.testinfra;

import com.example.homework.model.User;
import com.example.homework.repository.UserRepository;
import com.example.homework.service.UserRegistrationService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestHttpServer implements AutoCloseable {
    private final UserRegistrationService userRegistrationService;
    private final UserRepository userRepository;
    private HttpServer server;
    private ExecutorService executor;
    private int port;

    public TestHttpServer(UserRegistrationService userRegistrationService, UserRepository userRepository) {
        this.userRegistrationService = userRegistrationService;
        this.userRepository = userRepository;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        executor = Executors.newSingleThreadExecutor();
        server.setExecutor(executor);

        server.createContext("/api/register", this::handleRegister);
        server.createContext("/api/active", this::handleActiveCount);
        server.createContext("/", this::handleHomePage);
        server.createContext("/users", this::handleUsersPage);

        server.start();
        port = server.getAddress().getPort();
    }

    public URI baseUri() {
        return URI.create("http://localhost:" + port);
    }

    @Override
    public void close() {
        if (server != null) {
            server.stop(0);
        }
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    private void handleRegister(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed", "text/plain");
            return;
        }

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> params = parseForm(body);
        String email = params.get("email");
        String fullName = params.get("fullName");

        if (email == null || fullName == null) {
            sendText(exchange, 400, "Missing email or fullName", "text/plain");
            return;
        }

        try {
            User user = userRegistrationService.register(email, fullName);
            String json = "{\"id\":" + user.getId() + ",\"email\":\"" + user.getEmail() + "\"}";
            sendText(exchange, 201, json, "application/json");
        } catch (Exception exception) {
            sendText(exchange, 500, "Registration failed", "text/plain");
        }
    }

    private void handleActiveCount(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed", "text/plain");
            return;
        }

        try {
            int count = userRepository.findActiveUsers().size();
            String json = "{\"count\":" + count + "}";
            sendText(exchange, 200, json, "application/json");
        } catch (SQLException exception) {
            sendText(exchange, 500, "Count failed", "text/plain");
        }
    }

    private void handleHomePage(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed", "text/plain");
            return;
        }

        String html = "<html><head><title>Users</title></head><body><h1>User registration</h1></body></html>";
        sendText(exchange, 200, html, "text/html");
    }

    private void handleUsersPage(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendText(exchange, 405, "Method Not Allowed", "text/plain");
            return;
        }

        try {
            List<User> users = userRepository.findActiveUsers();
            StringBuilder html = new StringBuilder();
            html.append("<html><head><title>Active users</title></head><body><h1>Active users</h1><ul>");
            for (User user : users) {
                html.append("<li>").append(user.getEmail()).append("</li>");
            }
            html.append("</ul></body></html>");
            sendText(exchange, 200, html.toString(), "text/html");
        } catch (SQLException exception) {
            sendText(exchange, 500, "Users page failed", "text/plain");
        }
    }

    private static Map<String, String> parseForm(String body) {
        Map<String, String> params = new HashMap<>();
        if (body == null || body.isEmpty()) {
            return params;
        }
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            String key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8);
            String value = parts.length > 1 ? URLDecoder.decode(parts[1], StandardCharsets.UTF_8) : "";
            params.put(key, value);
        }
        return params;
    }

    private static void sendText(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] payload = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", contentType + "; charset=utf-8");
        exchange.sendResponseHeaders(status, payload.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(payload);
        }
    }
}
