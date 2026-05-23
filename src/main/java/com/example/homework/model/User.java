package com.example.homework.model;

public class User {
    private Long id;
    private String email;
    private String fullName;
    private String status;

    public User(Long id, String email, String fullName, String status) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.status = status;
    }

    public User(String email, String fullName, String status) {
        this(null, email, fullName, status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStatus() {
        return status;
    }
}
