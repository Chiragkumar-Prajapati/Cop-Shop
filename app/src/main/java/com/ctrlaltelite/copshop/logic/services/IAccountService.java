package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;

public interface IAccountService {

    /**
     *
     * @param email The email of the user to fetch AccountObject for
     * @return An AccountObject to represent the user if successfully logged in, else null
     */
    AccountObject fetchAccountByEmail(String email);

    /**
     * Used by the login page to check whether a user's login credentials are legit.
     * @param username The username of the user attempting to log in
     * @param password The password of the user attempting to log in
     * @return The BuyerAccountObject or SellerAccountObject, or null if not legit.
     */
    AccountObject validateEmailAndPassword(String username, String password);

    /**
     * Validate that the object has legal data and then add to the database.
     * @param buyerAccount The object to add to the DB.
     * @return the validation object we created to do the validation.
     */
    BuyerAccountValidationObject validate(BuyerAccountObject buyerAccount);

    /**
     * Validate that the object has legal data and then add to the database.
     * @param sellerAccount The object to add to the DB.
     * @return the validation object we created to do the validation.
     */
    SellerAccountValidationObject validate(SellerAccountObject sellerAccount);

    /**
     * Actually creates the BuyerAccount in the database
     * @param newBuyer The object to add to the DB
     * @return the primary key of the DB row
     */
    String registerNewBuyer(BuyerAccountObject newBuyer);

    /**
     * Updates the BuyerAccount in the database (and maintains uniqueness)
     * @param buyerAccount The object to update in the DB
     * @return whether the operation was successful
     */
    boolean updateBuyerAccount(String id, BuyerAccountObject buyerAccount);

    /**
     * Actually creates the SellerAccount in the database
     * @param newSeller The object to add to the DB
     * @return the primary key of the DB row
     */
    String registerNewSeller(SellerAccountObject newSeller);

    /**
     * Updates the SellerAccount in the database (and maintains uniqueness)
     * @param sellerAccount The object to update in the DB
     * @return whether the operation was successful
     */
    boolean updateSellerAccount(String id, SellerAccountObject sellerAccount);

    /**
     * Get the first name of the buyer
     * @param buyerId Id of buyer
     * @return String buyer's first name
     */
    String getBuyerName(String buyerId);

}
