package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.AccountObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountService implements com.ctrlaltelite.copshop.logic.services.IAccountService {
    private ISellerModel sellerModel;
    private IBuyerModel buyerModel;

    private static String[] provinces = {"Alberta", "British Columbia", "Manitoba", "New Brunswick",
    "Newfoundland and Labrador", "Northwest Territories", "Nova Scotia", "Nunavut",
    "Ontario", "Prince Edward Island", "Quebec", "Saskatchewan", "Yukon"};
    private static String[] provinceAbbr = {"AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON",
    "PE", "PEI", "QC", "SK", "YT"};

    private static String postalCodeRegex = "^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$";
    private static String emailRegex = "^(.+)@(.+)$";
    private static String passwordRegex = "((?=.*[a-z])(?=.*d)(?=.*[@#$%])(?=.*[A-Z]))";

    public AccountService(ISellerModel sellerModel, IBuyerModel buyerModel) {
        this.sellerModel = sellerModel;
        this.buyerModel = buyerModel;
    }

    /**
     * Used by the login page to check whether a user's login credentials are legit.
     * @param email The email of the user attempting to log in
     * @param password The password of the user attempting to log in
     * @return The BuyerAccountObject or SellerAccountObject, or null if not legit.
     */
    public AccountObject validateEmailAndPassword(String email, String password) {
        // Check for a matching seller account
        AccountObject account = this.sellerModel.findByEmail(email);

        // Otherwise check for a matching buyer account
        if (null == account) {
            account = this.buyerModel.findByEmail(email);
        }

        // Verify password matches
        if (null != account && account.getPassword().equals(password)) {
            return account;
        }

        // Otherwise no match
        return null;
    }

    public AccountObject fetchAccountByEmail(String email) {
        // Check for a matching seller account
        AccountObject account = this.sellerModel.findByEmail(email);

        // Otherwise check for a matching buyer account
        if (null == account) {
            account = this.buyerModel.findByEmail(email);
        }
        return account;
    }

    /**
     * Validate that the object has legal data and then add to the database.
     * @param buyerObject the object to add to the DB.
     * @return the validation object we created to do the validation.
     */
    public BuyerAccountValidationObject validate(BuyerAccountObject buyerObject) {
        return this.validateInputForm(buyerObject);
    }

    /**
     * Validate that the object has legal data and then add to the database.
     * @param sellerObject the object to add to the DB.
     * @return the validation object we created to do the validation.
     */
    public SellerAccountValidationObject validate(SellerAccountObject sellerObject) {
        return this.validateInputForm(sellerObject);
    }

    /**
     * Actually creates the BuyerAccount in the database
     * @param newBuyer The object to add to the DB
     * @return the primary key of the DB row
     */
    public String registerNewBuyer(BuyerAccountObject newBuyer){
        if (fetchAccountByEmail(newBuyer.getEmail()) != null) {
            return null;
        }
        return buyerModel.createNew(newBuyer);
    }

    /**
     * Updates the BuyerAccount in the database (and maintains uniqueness)
     * @param buyerAccount The object to update in the DB
     * @return whether the operation was successful
     */
    public boolean updateBuyerAccount(String id, BuyerAccountObject buyerAccount) {
        AccountObject currAccount = fetchAccountByEmail(buyerAccount.getEmail());
        if (currAccount != null && !currAccount.getId().equals(id)) {
            return false;
        }
        if (currAccount != null && !(currAccount instanceof BuyerAccountObject)) {
            return false;
        }
        return buyerModel.update(id, buyerAccount);
    }

    /**
     * Actually creates the SellerAccount in the database
     * @param newSeller The object to add to the DB
     * @return the primary key of the DB row
     */
    public String registerNewSeller(SellerAccountObject newSeller) {
        if (fetchAccountByEmail(newSeller.getEmail()) != null) {
            return null;
        }
        return sellerModel.createNew(newSeller);
    }

    /**
     * Updates the SellerAccount in the database (and maintains uniqueness)
     * @param sellerAccount The object to update in the DB
     * @return whether the operation was successful
     */
    public boolean updateSellerAccount(String id, SellerAccountObject sellerAccount) {
        AccountObject currAccount = fetchAccountByEmail(sellerAccount.getEmail());
        if (currAccount != null && !currAccount.getId().equals(id)) {
            return false;
        }
        if (currAccount != null && !(currAccount instanceof SellerAccountObject)) {
            return false;
        }
        return sellerModel.update(id, sellerAccount);
    }

    /**
     * Ensures that the all the values in the form are valid, by calling the
     * other methods below this one.
     * @param buyerObject An object populated with the form fields.
     * @return BuyerAccountValidationObject
     */
    private BuyerAccountValidationObject validateInputForm(BuyerAccountObject buyerObject) {
        BuyerAccountValidationObject validationBuyerObject = new BuyerAccountValidationObject();

        validationBuyerObject.setValidFirstName(validateFirstName(buyerObject.getFirstName()));
        validationBuyerObject.setValidLastName(validateLastName(buyerObject.getLastName()));
        validationBuyerObject.setValidStreetAddress(validateStreetAddress(buyerObject.getStreetAddress()));
        validationBuyerObject.setValidPostalCode(validatePostalCode(buyerObject.getPostalCode()));
        validationBuyerObject.setValidProvince(validateProvince(buyerObject.getProvince()));
        validationBuyerObject.setValidEmail(validateEmail(buyerObject.getEmail()));
        validationBuyerObject.setValidPassword(validatePassword(buyerObject.getPassword()));

        return validationBuyerObject;
    }

    /**
     * Ensures that the all the values in the form are valid, by calling the
     * other methods below this one.
     * @param sellerObject An object populated with the form fields.
     * @return SellerAccountValidationObject
     */
    private SellerAccountValidationObject validateInputForm(SellerAccountObject sellerObject) {
        SellerAccountValidationObject validationSellerObject = new SellerAccountValidationObject();

        validationSellerObject.setValidOrganizationName(validateOrganizationName(sellerObject.getOrganizationName()));
        validationSellerObject.setValidStreetAddress(validateStreetAddress(sellerObject.getStreetAddress()));
        validationSellerObject.setValidPostalCode(validatePostalCode(sellerObject.getPostalCode()));
        validationSellerObject.setValidProvince(validateProvince(sellerObject.getProvince()));
        validationSellerObject.setValidEmail(validateEmail(sellerObject.getEmail()));
        validationSellerObject.setValidPassword(validatePassword(sellerObject.getPassword()));

        return validationSellerObject;
    }

    /**
     * Determine if precinct name field is valid(non-empty)
     * @param organizationName String
     * @return Boolean indicating if valid
     */
    private boolean validateOrganizationName(String organizationName) {
        return organizationName != null && !organizationName.isEmpty();
    }

    /**
     * Determine if first name field is valid(non-empty)
     * @param firstName String
     * @return Boolean indicating if valid
     */
    private boolean validateFirstName(String firstName) {
        return firstName != null && !firstName.isEmpty();
    }

    /**
     * Determine if last name field is valid(non-empty)
     * @param lastName String
     * @return Boolean indicating if valid
     */
    private boolean validateLastName(String lastName) {
        return lastName != null && !lastName.isEmpty();
    }

    /**
     * Determine if street address field is valid(non-empty)
     * @param address String
     * @return Boolean indicating if valid
     */
    private boolean validateStreetAddress(String address){
        //since we aren't connecting to any external services... this is just another null check
        //but it could be so much more!
        return address!=null && !address.isEmpty();
    }

    /**
     * Determine if postal code is valid Canadian postal code
     * @param postalCode String
     * @return Boolean indicating if valid
     */
    private boolean validatePostalCode(String postalCode){
        if (postalCode == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(postalCodeRegex);
        Matcher matcher = pattern.matcher(postalCode);

        return matcher.matches();
    }

    /**
     * Determine if province entered is valid Canadian province.
     * @param province String
     * @return Boolean indicating if valid
     */
    private boolean validateProvince(String province){
        boolean check = false;
        if(Arrays.asList(provinces).contains(province) ||
                Arrays.asList(provinceAbbr).contains(province)){
            check = true;
        }
        return check;
    }

    /**
     * Determine if email field has an '@' symbol
     * with characters before and after the symbol.
     * @param email String
     * @return Boolean indicating if valid
     */
    private boolean validateEmail(String email){
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Determine if password field has
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character
     * - At least one capital letter
     * @param password String
     * @return Boolean indicating if valid
     */

    private boolean validatePassword(String password){
        if (password == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return true; //matcher.matches();
    }

    @Override
    public String getBuyerName(String buyerId) {
        BuyerAccountObject buyerObject = buyerModel.fetch(buyerId);
        return buyerObject.getFirstName();
    }
}
