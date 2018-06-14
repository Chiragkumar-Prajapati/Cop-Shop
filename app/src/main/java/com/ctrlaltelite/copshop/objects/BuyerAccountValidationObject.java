package com.ctrlaltelite.copshop.objects;

/**
 * Data object that stores (Boolean) values for
 * each field in the CreateBuyerAccount form indicating
 * whether or not data in it is valid
 */
public class BuyerAccountValidationObject {

    private boolean validFirstName;
    private boolean validLastName;
    private boolean validStreetAddress;
    private boolean validPostalCode;
    private boolean validProvince;
    private boolean validEmail;
    private boolean validPassword;

    public BuyerAccountValidationObject(){
        this.validFirstName = true;
        this.validLastName = true;
        this.validStreetAddress = true;
        this.validPostalCode = true;
        this.validProvince = true;
        this.validEmail = true;
        this.validPassword = true;
    }
    //Getters

    public boolean getValidFirstName() { return this.validFirstName; }

    public boolean getValidLastName() { return this.validLastName; }

    public boolean getValidStreetAddress() { return this.validStreetAddress; }

    public boolean getValidPostalCode() { return this.validPostalCode; }

    public boolean getValidProvince() {return this.validProvince;}

    public boolean getValidEmail() { return this.validEmail; }

    public boolean getValidPassword() { return this.validPassword; }

    //Setters

    public void setValidFirstName(boolean validFirstName) { this.validFirstName = validFirstName;}

    public void setValidLastName(boolean validLastName) {this.validLastName = validLastName; }

    public void setValidStreetAddress(boolean validStreetAddress) { this.validStreetAddress = validStreetAddress;}

    public void setValidPostalCode(boolean validPostalCode) {this.validPostalCode = validPostalCode; }

    public void setValidProvince(boolean validProvince) {this.validProvince = validProvince; }

    public void setValidEmail(boolean validEmail) {this.validEmail = validEmail; }

    public void setValidPassword(boolean validPassword) {this.validPassword = validPassword; }

    public boolean allValid(){
        return (validFirstName &&
                validLastName &&
                validStreetAddress &&
                validPostalCode &&
                validProvince &&
                validEmail &&
                validPassword);
    }
}
