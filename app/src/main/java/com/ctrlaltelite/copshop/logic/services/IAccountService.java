package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;

public interface IAccountService {

    /**
     *
     * @param username The username of the user attempting to log in
     * @param password The password of the user attempting to log in
     * @return An AccountObject to represent the user if successfully logged in, else null
     */
    AccountObject validateUsernameAndPassword (String username, String password);

    BuyerAccountValidationObject create(BuyerAccountObject buyerAccount);

    String registerNewBuyer(BuyerAccountObject newBuyer);

}
