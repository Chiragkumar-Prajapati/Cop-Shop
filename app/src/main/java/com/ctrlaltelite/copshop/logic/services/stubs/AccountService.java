package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.AccountObject;

public class AccountService implements com.ctrlaltelite.copshop.logic.services.IAccountService {
    private ISellerModel sellerModel;
    private IBuyerModel buyerModel;

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


}
