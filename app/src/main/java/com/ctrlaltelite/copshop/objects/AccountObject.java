package com.ctrlaltelite.copshop.objects;

public abstract class AccountObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String password;
    private String email;

    AccountObject(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
