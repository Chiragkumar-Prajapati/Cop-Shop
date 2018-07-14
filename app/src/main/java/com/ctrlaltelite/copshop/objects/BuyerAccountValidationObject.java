package com.ctrlaltelite.copshop.objects;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Data object that stores (Boolean) values for
 * each field in the CreateBuyerAccount form indicating
 * whether or not data in it is valid
 */
public class BuyerAccountValidationObject {
    private AccountValidationObject accountValidationObject;
    private boolean validFirstName;
    private boolean validLastName;

    public BuyerAccountValidationObject(AccountValidationObject accountValidationObject) {
        this.validFirstName = true;
        this.validLastName = true;
        this.accountValidationObject = accountValidationObject;
    }
    //Getters

    public boolean getValidEmail() { return this.accountValidationObject.getValidEmail(); }

    public boolean getValidPassword() { return this.accountValidationObject.getValidPassword(); }

    public boolean getValidFirstName() { return this.validFirstName; }

    public boolean getValidLastName() { return this.validLastName; }

    public boolean getValidStreetAddress() { return this.accountValidationObject.getValidStreetAddress(); }

    public boolean getValidPostalCode() { return this.accountValidationObject.getValidPostalCode(); }

    public boolean getValidProvince() {return this.accountValidationObject.getValidProvince();}

    //Setters

    public void setValidEmail(boolean validEmail) {this.accountValidationObject.setValidEmail(validEmail);}

    public void setValidPassword(boolean validPassword) {this.accountValidationObject.setValidPassword(validPassword);}

    public void setValidFirstName(boolean validFirstName) { this.validFirstName = validFirstName;}

    public void setValidLastName(boolean validLastName) {this.validLastName = validLastName; }

    public void setValidStreetAddress(boolean validStreetAddress) { this.accountValidationObject.setValidStreetAddress(validStreetAddress);}

    public void setValidPostalCode(boolean validPostalCode) {this.accountValidationObject.setValidPostalCode(validPostalCode);}

    public void setValidProvince(boolean validProvince) {this.accountValidationObject.setValidProvince(validProvince);}

    public void setAll(boolean valid) {
        this.validFirstName = valid;
        this.validLastName = valid;
        this.accountValidationObject.setAll(valid);
    }

    public boolean allValid(){
        return (this.accountValidationObject.allValid() &&
                validFirstName &&
                validLastName);
    }

    public void validate(BuyerAccountObject buyerObject) {
        this.accountValidationObject.validate(buyerObject);
        this.validateFirstName(buyerObject.getFirstName());
        this.validateLastName(buyerObject.getLastName());
    }

    /**
     * Determine if first name field is valid(non-empty)
     * @param firstName String
     */
    public void validateFirstName(String firstName) {
        setValidFirstName(firstName != null && !firstName.isEmpty());
    }

    /**
     * Determine if last name field is valid(non-empty)
     * @param lastName String
     */
    public void validateLastName(String lastName) {
        setValidLastName(lastName != null && !lastName.isEmpty());
    }
}
