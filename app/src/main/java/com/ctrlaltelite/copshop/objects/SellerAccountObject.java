package com.ctrlaltelite.copshop.objects;

public class SellerAccountObject extends AccountObject {

    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String organizationName;
    private String streetAddress;
    private String postalCode;
    private String province;
    private String email;
    private String password;

    public SellerAccountObject(String id, String organizationName, String streetAddress,
                               String postalCode, String province, String email, String password) {
        super(id, password, email);
        this.id = id;
        this.organizationName = organizationName;
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

    public String getOrganizationName() {
        return organizationName;
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

    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

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
