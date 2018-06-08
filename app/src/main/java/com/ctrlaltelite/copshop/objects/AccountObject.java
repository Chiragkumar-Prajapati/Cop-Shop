package com.ctrlaltelite.copshop.objects;

public abstract class AccountObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String username;
    private String password;
    private String email;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
