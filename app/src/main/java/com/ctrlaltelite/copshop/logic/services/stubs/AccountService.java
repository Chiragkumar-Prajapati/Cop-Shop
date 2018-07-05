package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;
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
     * Find a buyer account by a given email
     * @param email String email to look for
     * @return BuyerAccountObject
     */
    private BuyerAccountObject fetchBuyerAccountByEmail(String email) {
        // Check for a matching buyer account
        return this.buyerModel.findByEmail(email);
    }

    /**
     * Find a seller account by a given email
     * @param email String email to look for
     * @return SellerAccountObject
     */
    private SellerAccountObject fetchSellerAccountByEmail(String email) {
        return this.sellerModel.findByEmail(email);
    }
    
    public BuyerAccountValidationObject validate(BuyerAccountObject buyerObject) {
        return this.validateInputForm(buyerObject);
    }

    public SellerAccountValidationObject validate(SellerAccountObject sellerObject) {
        return this.validateInputForm(sellerObject);
    }

    public String registerNewBuyer(BuyerAccountObject newBuyer){
        if (fetchBuyerAccountByEmail(newBuyer.getEmail()) != null) {
            return null;
        }
        return buyerModel.createNew(newBuyer);
    }

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

    public String registerNewSeller(SellerAccountObject newSeller) {
        if (fetchSellerAccountByEmail(newSeller.getEmail()) != null) {
            return null;
        }
        return sellerModel.createNew(newSeller);
    }

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

        validationBuyerObject.validateFirstName(buyerObject.getFirstName());
        validationBuyerObject.validateLastName(buyerObject.getLastName());
        validationBuyerObject.validateStreetAddress(buyerObject.getStreetAddress());
        validationBuyerObject.validatePostalCode(buyerObject.getPostalCode());
        validationBuyerObject.validateProvince(buyerObject.getProvince());
        validationBuyerObject.validateEmail(buyerObject.getEmail());
        validationBuyerObject.validatePassword(buyerObject.getPassword());

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

        validationSellerObject.validateOrganizationName(sellerObject.getOrganizationName());
        validationSellerObject.validateStreetAddress(sellerObject.getStreetAddress());
        validationSellerObject.validatePostalCode(sellerObject.getPostalCode());
        validationSellerObject.validateProvince(sellerObject.getProvince());
        validationSellerObject.validateEmail(sellerObject.getEmail());
        validationSellerObject.validatePassword(sellerObject.getPassword());

        return validationSellerObject;
    }

    @Override
    public String getBuyerName(String buyerId) {
        BuyerAccountObject buyerObject = buyerModel.fetch(buyerId);
        if (null == buyerObject) {
            return null;
        }
        return buyerObject.getFirstName();
    }
}
