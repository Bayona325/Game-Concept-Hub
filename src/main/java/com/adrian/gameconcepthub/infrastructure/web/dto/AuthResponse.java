package com.adrian.gameconcepthub.infrastructure.web.dto;

public class AuthResponse {
    private Long id;
    private String username;
    private String email;
    private String message;
    private boolean success;

    public AuthResponse() {
    }

    public AuthResponse(Long id, String username, String email, String message, boolean success) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.message = message;
        this.success = success;
    }

    public static AuthResponse success(Long id, String username, String email, String message) {
        return new AuthResponse(id, username, email, message, true);
    }

    public static AuthResponse error(String message) {
        return new AuthResponse(null, null, null, message, false);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
