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
                              String postalCode, String provice, String email, String password) {
        super(id, firstName, password, email);

    }



}
