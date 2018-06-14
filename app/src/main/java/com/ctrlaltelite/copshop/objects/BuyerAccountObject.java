package com.ctrlaltelite.copshop.objects;

public class BuyerAccountObject extends AccountObject {

    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String postalCode;
    private String province;
    private String email;
    private String password;

    public BuyerAccountObject(String id, String firstName, String lastName, String streetAddress,
                              String postalCode, String province, String email, String password) {
        super(id, password, email);

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.province = province;
        this.email = email;
        this.password = password;
    }

    //Getters

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreetAddress() {return streetAddress; }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public String getEmail() { return email;}

    public String getPassword() {
        return password;
    }

    //Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress; }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setProvince(String province) {
        this.province= province;
    }

    public void setEmail(String email) { this.email = email;}

    public void setPassword(String password) { this.password = password;}

}
