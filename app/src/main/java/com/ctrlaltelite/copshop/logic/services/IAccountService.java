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
     * Actually creates the BuyerAccount in the database
     * @param newBuyer The object to add to the DB
     * @return validation object containing success information for presentation feedback.
     */
    BuyerAccountValidationObject registerNewBuyer(BuyerAccountObject newBuyer);

    /**
     * Actually creates the SellerAccount in the database
     * @param newSeller The object to add to the DB
     * @return validation object containing success information for presentation feedback.
     */
    SellerAccountValidationObject registerNewSeller(SellerAccountObject newSeller);

    /**
     * Updates the BuyerAccount in the database (and maintains uniqueness)
     * @param buyerAccount The object to update in the DB
     * @return validation object containing success information for presentation feedback
     */
    BuyerAccountValidationObject updateBuyerAccount(String id, BuyerAccountObject buyerAccount);

    /**
     * Updates the SellerAccount in the database (and maintains uniqueness)
     * @param sellerAccount The object to update in the DB
     * @return validation object containing success information for presentation feedback
     */
    SellerAccountValidationObject updateSellerAccount(String id, SellerAccountObject sellerAccount);

    /**
     * Get the first name of the buyer
     * @param buyerId Id of buyer
     * @return String buyer's first name
     */
    String getBuyerName(String buyerId);

}
