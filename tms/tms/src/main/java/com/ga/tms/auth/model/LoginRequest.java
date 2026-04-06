package com.ga.tms.auth.model;

public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPasswordhash() {
        return password;
    }
}