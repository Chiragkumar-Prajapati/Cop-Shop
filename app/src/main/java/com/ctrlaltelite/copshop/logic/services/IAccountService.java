package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;

public interface IAccountService {

    /**
     *
     * @param username The username of the user attempting to log in
     * @param password The password of the user attempting to log in
     * @return An AccountObject to represent the user if successfully logged in, else null
     */
    AccountObject validateEmailAndPassword(String username, String password);

    AccountObject fetchAccountByEmail(String email);

    BuyerAccountValidationObject validate(BuyerAccountObject buyerAccount);

    SellerAccountValidationObject validate(SellerAccountObject sellerAccount);

    String registerNewBuyer(BuyerAccountObject newBuyer);

    String registerNewSeller(SellerAccountObject newSeller);

}
