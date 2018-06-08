package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.logic.CopShopApp;
import com.ctrlaltelite.copshop.objects.AccountObject;

public class AccountService implements com.ctrlaltelite.copshop.logic.services.IAccountService {

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
