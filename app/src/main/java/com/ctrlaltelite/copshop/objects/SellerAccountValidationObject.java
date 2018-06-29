package com.ctrlaltelite.copshop.objects;

/**
 * Data object that stores (Boolean) values for
 * each field in the CreateBuyerAccount form indicating
 * whether or not data in it is valid
 */
public class SellerAccountValidationObject {

    private boolean validOrganizationName;
    private boolean validStreetAddress;
    private boolean validPostalCode;
    private boolean validProvince;
    private boolean validEmail;
    private boolean validPassword;

    public SellerAccountValidationObject(){
        this.validOrganizationName = true;
        this.validStreetAddress = true;
        this.validPostalCode = true;
        this.validProvince = true;
        this.validEmail = true;
        this.validPassword = true;
    }
    //Getters

    public boolean getValidOrganizationName() { return this.validOrganizationName; }

    public boolean getValidStreetAddress() { return this.validStreetAddress; }

    public boolean getValidPostalCode() { return this.validPostalCode; }

    public boolean getValidProvince() {return this.validProvince;}

    public boolean getValidEmail() { return this.validEmail; }

    public boolean getValidPassword() { return this.validPassword; }

    //Setters

    public void setValidOrganizationName(boolean validOrganizationName) { this.validOrganizationName = validOrganizationName;}

    public void setValidStreetAddress(boolean validStreetAddress) { this.validStreetAddress = validStreetAddress;}

    public void setValidPostalCode(boolean validPostalCode) {this.validPostalCode = validPostalCode; }

    public void setValidProvince(boolean validProvince) {this.validProvince = validProvince; }

    public void setValidEmail(boolean validEmail) {this.validEmail = validEmail; }

    public void setValidPassword(boolean validPassword) {this.validPassword = validPassword; }

    public boolean allValid(){
        return (validOrganizationName &&
                validStreetAddress &&
                validPostalCode &&
                validProvince &&
                validEmail &&
                validPassword);
    }
}
