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

    private static final String[] provinces = {"Alberta", "British Columbia", "Manitoba", "New Brunswick",
            "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", "Nunavut",
            "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"};
    private static final String[] provinceAbbr = {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON",
            "PE", "PEI", "QC", "SK", "YT"};

    private static final String postalCodeRegex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
    private static final String emailRegex = "^(.+)@(.+)$";
    private static final String passwordRegex = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]))";

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

    private void setValidFirstName(boolean validFirstName) { this.validFirstName = validFirstName;}

    private void setValidLastName(boolean validLastName) {this.validLastName = validLastName; }

    private void setValidStreetAddress(boolean validStreetAddress) { this.validStreetAddress = validStreetAddress;}

    private void setValidPostalCode(boolean validPostalCode) {this.validPostalCode = validPostalCode; }

    private void setValidProvince(boolean validProvince) {this.validProvince = validProvince; }

    private void setValidEmail(boolean validEmail) {this.validEmail = validEmail; }

    private void setValidPassword(boolean validPassword) {this.validPassword = validPassword; }

    public boolean allValid(){
        return (validFirstName &&
                validLastName &&
                validStreetAddress &&
                validPostalCode &&
                validProvince &&
                validEmail &&
                validPassword);
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

    /**
     * Determine if street address field is valid(non-empty)
     * @param address String
     */
    public void validateStreetAddress(String address){
        //since we aren't connecting to any external services... this is just another null check
        //but it could be so much more!
        setValidStreetAddress(address!=null && !address.isEmpty());
    }

    /**
     * Determine if postal code is valid Canadian postal code
     * @param postalCode String
     */
    public void validatePostalCode(String postalCode){
        if (postalCode == null) {
            setValidPostalCode(false);
        }
        else {
            Pattern pattern = Pattern.compile(postalCodeRegex);
            Matcher matcher = pattern.matcher(postalCode);
            setValidPostalCode(matcher.matches());
        }
    }

    /**
     * Determine if province entered is valid Canadian province.
     * @param province String
     */
    public void validateProvince(String province){
        boolean check = false;
        if(Arrays.asList(provinces).contains(province) ||
                Arrays.asList(provinceAbbr).contains(province)){
            check = true;
        }
        setValidProvince(check);
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
