package com.ctrlaltelite.copshop.objects;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressValidationObject {
    private static final String[] provinces = {"alberta", "british columbia", "manitoba", "new brunswick",
            "newfoundland and labrador", "northwest Territories", "nova scotia", "nunavut",
            "ontario", "prince edward island", "quebec", "saskatchewan", "yukon"};
    private static final String[] provinceAbbr = {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON",
            "PE", "PEI", "QC", "SK", "YT"};

    private static final String postalCodeRegex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";

    private boolean validStreetAddress;
    private boolean validPostalCode;
    private boolean validProvince;

    public AddressValidationObject() {
        this.validStreetAddress = true;
        this.validPostalCode = true;
        this.validProvince = true;
    }

    public boolean getValidStreetAddress() { return this.validStreetAddress; }

    public boolean getValidPostalCode() { return this.validPostalCode; }

    public boolean getValidProvince() {return this.validProvince;}

    public void setValidStreetAddress(boolean validStreetAddress) { this.validStreetAddress = validStreetAddress;}

    public void setValidPostalCode(boolean validPostalCode) {this.validPostalCode = validPostalCode; }

    public void setValidProvince(boolean validProvince) {this.validProvince = validProvince; }

    public void setAll(boolean valid) {
        this.validStreetAddress = valid;
        this.validPostalCode = valid;
        this.validProvince = valid;
    }

    public boolean allValid(){
        return (validStreetAddress &&
                validPostalCode &&
                validProvince);
    }

    public void validate(AddressObject address) {
        this.validateStreetAddress(address.getStreetAddress());
        this.validatePostalCode(address.getPostalCode());
        this.validateProvince(address.getProvince());
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
            Matcher matcher = pattern.matcher(postalCode.toUpperCase());
            setValidPostalCode(matcher.matches());
        }
    }

    /**
     * Determine if province entered is valid Canadian province.
     * @param province String
     */
    public void validateProvince(String province) {
        boolean check = false;
        if(null != province && (Arrays.asList(provinces).contains(province.toLowerCase()) ||
                Arrays.asList(provinceAbbr).contains(province.toUpperCase()))) {
            check = true;
        }
        setValidProvince(check);
    }
}
