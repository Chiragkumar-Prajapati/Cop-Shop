package com.ctrlaltelite.copshop.logic;

import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.presentation.interfaces.IAccountService;

public class AccountService implements IAccountService {
    private AccountService currentService;

    public AccountService AccountService(){
        currentService = this;

        return currentService;
    }

    public AccountObject validateUsernameAndPassword (String username, String password) {
        AccountObject user = null;

        // First look for a matching seller account
        if (CopShopApp.sellerModel.checkUsernamePasswordMatch(username, password) ) {
            user = CopShopApp.sellerModel.findByUsername(username);
        }

        // If no luck, look for a matching buyer account
        if (user == null) {
            if (CopShopApp.buyerModel.checkUsernamePasswordMatch(username, password) ) {
                user = CopShopApp.buyerModel.findByUsername(username);
            }
        }

        return user;
    }


}
