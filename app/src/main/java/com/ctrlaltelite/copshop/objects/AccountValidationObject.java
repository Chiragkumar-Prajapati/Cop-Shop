package com.ctrlaltelite.copshop.objects;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountValidationObject {
    private static final String emailRegex = "^(.+)@(.+)$";
    private static final String passwordRegex = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]))";

    private boolean validEmail;
    private boolean validPassword;
    private AddressValidationObject addressValidationObject;

    public AccountValidationObject(AddressValidationObject addressValidationObject) {
        this.validEmail = true;
        this.validPassword = true;
        this.addressValidationObject = addressValidationObject;
    }

    // Getters

    public boolean getValidEmail() { return this.validEmail; }

    public boolean getValidPassword() { return this.validPassword; }

    public boolean getValidStreetAddress() { return this.addressValidationObject.getValidStreetAddress(); }

    public boolean getValidPostalCode() { return this.addressValidationObject.getValidPostalCode(); }

    public boolean getValidProvince() {return this.addressValidationObject.getValidProvince();}

    // Setters

    public void setValidEmail(boolean validEmail) {this.validEmail = validEmail; }

    public void setValidPassword(boolean validPassword) {this.validPassword = validPassword; }

    public void setValidStreetAddress(boolean validStreetAddress) { this.addressValidationObject.setValidStreetAddress(validStreetAddress);}

    public void setValidPostalCode(boolean validPostalCode) {this.addressValidationObject.setValidPostalCode(validPostalCode); }

    public void setValidProvince(boolean validProvince) {this.addressValidationObject.setValidProvince(validProvince); }

    public void setAll(boolean valid) {
        this.validEmail = valid;
        this.validPassword = valid;
        this.addressValidationObject.setAll(valid);
    }

    public boolean allValid(){
        return (validEmail &&
                validPassword &&
                this.addressValidationObject.allValid());
    }

    public void validate(AccountObject account) {
        this.validateEmail(account.getEmail());
        this.validatePassword(account.getPassword());
        this.addressValidationObject.validate(account.getAddress());
    }

    /**
     * Determine if email field has an '@' symbol
     * with characters before and after the symbol.
     * @param email String
     */
    public void validateEmail(String email) {
        if (email == null) {
            setValidEmail(false);
        } else {
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            setValidEmail(matcher.matches());
        }
    }

    /**
     * Determine if password field has
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     * - At least one capital letter
     * @param password String

     */
    public void validatePassword(String password){
        if (password == null) {
            setValidPassword(false);
        }
        else {
            Pattern pattern = Pattern.compile(passwordRegex);
            Matcher matcher = pattern.matcher(password);
            setValidPassword(true); //matcher.matches();
        }
    }
}
