package com.ctrlaltelite.copshop.objects;

public class BuyerAccountObject extends AccountObject {

    private String id; // Only ever set by the database. Is generated when saving a new account.
    private String firstName;
    private String lastName;

    public BuyerAccountObject(String id, String firstName, String lastName, String streetAddress,
                              String postalCode, String province, String email, String password) {
        super(id, password, email, new AddressObject(streetAddress, province, postalCode));
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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

}
