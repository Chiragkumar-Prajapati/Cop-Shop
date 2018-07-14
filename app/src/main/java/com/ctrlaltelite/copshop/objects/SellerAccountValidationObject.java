package com.ctrlaltelite.copshop.objects;

/**
 * Data object that stores (Boolean) values for
 * each field in the CreateBuyerAccount form indicating
 * whether or not data in it is valid
 */
public class SellerAccountValidationObject {
    private boolean validOrganizationName;
    private AccountValidationObject accountValidationObject;

    public SellerAccountValidationObject(AccountValidationObject accountValidationObject) {
        this.validOrganizationName = true;
        this.accountValidationObject = accountValidationObject;
    }

    //Getters

    public boolean getValidEmail() { return this.accountValidationObject.getValidEmail(); }

    public boolean getValidPassword() { return this.accountValidationObject.getValidPassword(); }

    public boolean getValidOrganizationName() { return this.validOrganizationName; }

    public boolean getValidStreetAddress() { return this.accountValidationObject.getValidStreetAddress(); }

    public boolean getValidPostalCode() { return this.accountValidationObject.getValidPostalCode(); }

    public boolean getValidProvince() {return this.accountValidationObject.getValidProvince();}

    //Setters

    public void setValidEmail(boolean validEmail) {this.accountValidationObject.setValidEmail(validEmail);}

    public void setValidPassword(boolean validPassword) {this.accountValidationObject.setValidPassword(validPassword);}

    public void setValidOrganizationName(boolean validOrganizationName) { this.validOrganizationName = validOrganizationName;}

    public void setValidStreetAddress(boolean validStreetAddress) { this.accountValidationObject.setValidStreetAddress(validStreetAddress);}

    public void setValidPostalCode(boolean validPostalCode) {this.accountValidationObject.setValidPostalCode(validPostalCode);}

    public void setValidProvince(boolean validProvince) {this.accountValidationObject.setValidProvince(validProvince);}

    public void setAll(boolean valid) {
        this.accountValidationObject.setAll(valid);
        this.validOrganizationName = valid;
    }

    public boolean allValid(){
        return (this.accountValidationObject.allValid() &&
                validOrganizationName);
    }

    public void validate(SellerAccountObject sellerObject) {
        this.accountValidationObject.validate(sellerObject);
        this.validateOrganizationName(sellerObject.getOrganizationName());
    }

    /**
     * Determine if precinct name field is valid(non-empty)
     * @param organizationName String
     */
    public void validateOrganizationName(String organizationName) {
        setValidOrganizationName(organizationName != null && !organizationName.isEmpty());
    }
}
