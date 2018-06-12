package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.objects.SellerAccountObject;
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
        // Check for a matching buyer account
        AccountObject account = sellerModel.findByUsername(username);
        // Otherwise check for a matching seller account
        if (null == account) {
            account = buyerModel.findByUsername(username);
        }
        // Verify password matches
        if (null != account && account.getPassword().equals(password)) {
            return account;
        }
        // Otherwise no match
        return null;
    }


}
