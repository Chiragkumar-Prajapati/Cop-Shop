package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.objects.AccountValidationObject;
import com.ctrlaltelite.copshop.objects.AddressValidationObject;
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

    public BuyerAccountValidationObject registerNewBuyer(BuyerAccountObject newBuyer){
        BuyerAccountValidationObject validation = this.validateInputForm(newBuyer);

        // Has an account been registered with this email before?
        if (!(this.sellerModel.findByEmail(newBuyer.getEmail()) == null && this.buyerModel.findByEmail(newBuyer.getEmail()) == null)) {
            validation.setValidEmail(false);

        } else if(validation.allValid()) {
            String newId = buyerModel.createNew(newBuyer);
            if (null == newId) {
                validation.setAll(false);
            }
        }

        return validation;
    }

    public SellerAccountValidationObject registerNewSeller(SellerAccountObject newSeller) {
        SellerAccountValidationObject validation = this.validateInputForm(newSeller);

        // Has an account been registered with this email before?
        if (!(this.sellerModel.findByEmail(newSeller.getEmail()) == null && this.buyerModel.findByEmail(newSeller.getEmail()) == null)) {
            validation.setValidEmail(false);

        } else if(validation.allValid()) {
            String newId = sellerModel.createNew(newSeller);
            if (null == newId) {
                validation.setAll(false);
            }
        }

        return validation;
    }

    public BuyerAccountValidationObject updateBuyerAccount(String id, BuyerAccountObject buyerAccount) {
        BuyerAccountObject thisAccount = buyerModel.fetch(id);
        AccountObject currAccount = fetchAccountByEmail(thisAccount.getEmail());
        BuyerAccountValidationObject validation = this.validateInputForm(buyerAccount);

        if(currAccount != null && validation.allValid() && currAccount.getId().equals(id)) {
            boolean success = buyerModel.update(id, buyerAccount);
            if (!success) {
                validation.setAll(false);
            }
        }

        return validation;
    }

    public SellerAccountValidationObject updateSellerAccount(String id, SellerAccountObject sellerAccount) {
        SellerAccountObject thisAccount = sellerModel.fetch(id);
        AccountObject currAccount = fetchAccountByEmail(thisAccount.getEmail());
        SellerAccountValidationObject validation = this.validateInputForm(sellerAccount);

        if(currAccount != null && validation.allValid() && currAccount.getId().equals(id)) {
            boolean success = sellerModel.update(id, sellerAccount);
            if (!success) {
                validation.setAll(false);
            }
        }

        return validation;
    }

    /**
     * Ensures that the all the values in the form are valid, by calling the
     * other methods below this one.
     * @param buyerObject An object populated with the form fields.
     * @return BuyerAccountValidationObject
     */
    private BuyerAccountValidationObject validateInputForm(BuyerAccountObject buyerObject) {
        AddressValidationObject addressValidationObject = new AddressValidationObject();
        AccountValidationObject accountValidationObject = new AccountValidationObject(addressValidationObject);
        BuyerAccountValidationObject validationBuyerObject = new BuyerAccountValidationObject(accountValidationObject);
        if (null == buyerObject) {
            validationBuyerObject.setAll(false);
        } else {
            validationBuyerObject.validate(buyerObject);
        }
        return validationBuyerObject;
    }

    /**
     * Ensures that the all the values in the form are valid
     * @param sellerObject An object populated with the form fields.
     * @return SellerAccountValidationObject
     */
    private SellerAccountValidationObject validateInputForm(SellerAccountObject sellerObject) {
        AddressValidationObject addressValidationObject = new AddressValidationObject();
        AccountValidationObject accountValidationObject = new AccountValidationObject(addressValidationObject);
        SellerAccountValidationObject validationSellerObject = new SellerAccountValidationObject(accountValidationObject);
        if (null == sellerObject) {
            validationSellerObject.setAll(false);
        } else {
            validationSellerObject.validate(sellerObject);
        }
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
