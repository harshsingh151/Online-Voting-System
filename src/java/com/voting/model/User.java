package com.voting.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password; // Note: In a real system, we only store the HASH of the password here
    private boolean hasVoted;

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor (for registration)
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.hasVoted = false; // Default state
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(boolean hasVoted) {
        this.hasVoted = hasVoted;
    }
}