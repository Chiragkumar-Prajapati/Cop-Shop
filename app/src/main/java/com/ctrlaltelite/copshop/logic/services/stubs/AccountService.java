package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.AccountObject;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountService implements com.ctrlaltelite.copshop.logic.services.IAccountService {
    private ISellerModel sellerModel;
    private IBuyerModel buyerModel;
    private static BuyerAccountValidationObject validationBuyerObject;
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

    public AccountObject validateUsernameAndPassword (String username, String password) {
        AccountObject user = null;

        // First look for a matching seller account
        if (sellerModel.checkUsernamePasswordMatch(username, password) ) {
            user = sellerModel.findByUsername(username);
        }
        // If no luck, look for a matching buyer account
        if (user == null) {
            if (buyerModel.checkUsernamePasswordMatch(username, password) ) {
                user = buyerModel.findByUsername(username);
            }
        }

        return user;
    }

    public BuyerAccountValidationObject create(BuyerAccountObject buyerObject) {
        validationBuyerObject = validateInputForm(buyerObject);
        return validationBuyerObject;
    }

    public String registerNewBuyer(BuyerAccountObject newBuyer){
        return buyerModel.createNew(newBuyer);
    }

    public BuyerAccountValidationObject validateInputForm(BuyerAccountObject buyerObject) {

        validationBuyerObject.setValidFirstName(validateFirstName(buyerObject.getFirstName()));
        validationBuyerObject.setValidLastName(validateLastName(buyerObject.getLastName()));
        validationBuyerObject.setValidStreetAddress(validateStreetAddress(buyerObject.getStreetAddress()));
        validationBuyerObject.setValidPostalCode(validatePostalCode(buyerObject.getPostalCode()));
        validationBuyerObject.setValidProvince(validateProvince(buyerObject.getProvince()));
        validationBuyerObject.setValidEmail(validateEmail(buyerObject.getEmail()));
        validationBuyerObject.setValidPassword(validatePassword(buyerObject.getPassword()));
        //TODO : write tests for each field in BuyerAccountObject
        return validationBuyerObject;
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
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
