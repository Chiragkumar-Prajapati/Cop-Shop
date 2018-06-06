package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.logic.AccountService;
import com.ctrlaltelite.copshop.presentation.interfaces.IAccountService;
import com.ctrlaltelite.copshop.logic.interfaces.IBuyerModel;
import com.ctrlaltelite.copshop.logic.interfaces.ISellerModel;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.models.BuyerModel;
import com.ctrlaltelite.copshop.persistence.models.SellerModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class AccountServiceTests {

    @Test
    public void validateUsernameAndPassword_verifiesLoginProperly() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);
        ISellerModel sellerModel = new SellerModel(database);
        IAccountService accountService = new AccountService();

        // Create an account
        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "pass1", "email1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2", "pass2", "email2");
        String id1 = buyerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);

        // Try pulling some accounts, given some creds
        AccountObject buyer1 = accountService.validateUsernameAndPassword("name1", "pass1");
        AccountObject buyer2 = accountService.validateUsernameAndPassword("nameWrong", "pass1");
        AccountObject buyer3 = accountService.validateUsernameAndPassword("name1", "passWrong");

        // Check credential pairs
        assertTrue("Did not verify correct credentials", buyer1 != null);
        assertTrue("Did not verify correct credentials", buyer1.getClass().isInstance(BuyerAccountObject.class));
        assertTrue("Verified false credentials", buyer2 == null);
        assertTrue("Verified false credentials", buyer3 == null);

        // Try pulling some accounts, given some creds
        AccountObject seller1 = accountService.validateUsernameAndPassword("name2", "pass2");
        AccountObject seller2 = accountService.validateUsernameAndPassword("nameWrong", "pass2");
        AccountObject seller3 = accountService.validateUsernameAndPassword("name2", "passWrong");

        // Check credential pairs
        assertTrue("Did not verify correct credentials", seller1 != null);
        assertTrue("Did not verify correct credentials", seller1.getClass().isInstance(SellerAccountObject.class));
        assertTrue("Verified false credentials", seller2 == null);
        assertTrue("Verified false credentials", seller3 == null);
    }
}
