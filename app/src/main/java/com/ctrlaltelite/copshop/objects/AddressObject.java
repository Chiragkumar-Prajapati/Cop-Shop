package com.ctrlaltelite.copshop.objects;

public class AddressObject {
    private String streetAddress;
    private String postalCode;
    private String province;

    public AddressObject(String streetAddress, String province, String postalCode) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.province = province;
    }

    public String getStreetAddress() {return streetAddress; }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProvince() {
        return province;
    }

    public void setStreetAddress(String streetAddress) {this.streetAddress = streetAddress; }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setProvince(String province) {
        this.province= province;
    }
}
