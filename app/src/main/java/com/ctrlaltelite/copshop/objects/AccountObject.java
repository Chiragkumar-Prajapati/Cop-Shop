package com.ctrlaltelite.copshop.objects;

public abstract class AccountObject {
    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String password;
    private String email;
    private AddressObject address;

    AccountObject(String id, String password, String email, AddressObject address) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.address = address;
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

    public AddressObject getAddress() {
        return this.address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAddress(AddressObject address) {
        this.address = address;
    }
}
