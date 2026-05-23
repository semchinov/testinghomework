package com.example.homework.fixtures;

import com.example.homework.model.User;

public final class UserFixture {
    private UserFixture() {
    }

    public static User defaultActiveUser() {
        return new User("alice@example.com", "Alice Johnson", "ACTIVE");
    }

    public static User inactiveUser() {
        return new User("bob@example.com", "Bob Stone", "INACTIVE");
    }
}
