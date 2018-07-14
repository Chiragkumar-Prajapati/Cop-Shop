package com.ctrlaltelite.copshop.objects;

public class SellerAccountObject extends AccountObject {

    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String organizationName;

    public SellerAccountObject(String id, String organizationName, String streetAddress,
                               String postalCode, String province, String email, String password) {
        super(id, password, email, new AddressObject(streetAddress, province, postalCode));
        this.id = id;
        this.organizationName = organizationName;
    }

    //Getters

    public String getId() {
        return id;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    //Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

}
