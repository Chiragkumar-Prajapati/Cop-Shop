package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.BuyerModel;
import com.ctrlaltelite.copshop.persistence.stubs.SellerModel;
import org.junit.Test;

import static org.junit.Assert.*;


public class AccountServiceTests {

    @Test
    public void validateUsernameAndPassword_verifiesLoginProperly() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);
        ISellerModel sellerModel = new SellerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        // Create an account
        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String id1 = buyerModel.createNew(account1);

        // Try pulling some accounts, given some creds
        AccountObject buyer1 = accountService.validateEmailAndPassword("email1", "pass1");
        AccountObject buyer2 = accountService.validateEmailAndPassword("emailWrong", "pass1");
        AccountObject buyer3 = accountService.validateEmailAndPassword("email1", "passWrong");

        // Check credential pairs
        assertTrue("Did not validate correct credentials", buyer1 != null);
        assertTrue("Returned wrong object type", buyer1 instanceof BuyerAccountObject);
        assertTrue("Verified false credentials", buyer2 == null);
        assertTrue("Verified false credentials", buyer3 == null);

    }

    @Test
    public void registerNewBuyer_savesBuyerAndReturnsID(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject account = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");

        // Save the new listings
        String id1 = accountService.registerNewBuyer(account);
        String id2 = accountService.registerNewBuyer(account);
        String id3 = accountService.registerNewBuyer(account);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Buyers", id1));
        assertTrue("Row was not created", database.rowExists("Buyers", id2));
        assertTrue("Row was not created", database.rowExists("Buyers", id3));

    }

    @Test
    public void registerNewSeller_savesSellerAndReturnsID(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        SellerAccountObject account = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");

        // Save the new listings
        String id1 = accountService.registerNewSeller(account);
        String id2 = accountService.registerNewSeller(account);
        String id3 = accountService.registerNewSeller(account);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Sellers", id1));
        assertTrue("Row was not created", database.rowExists("Sellers", id2));
        assertTrue("Row was not created", database.rowExists("Sellers", id3));

    }

    @Test
    public void createBuyer_verifiesValidationObjectCreation(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);
        BuyerAccountValidationObject validationObject;
        BuyerAccountObject validAccountInfo;
        BuyerAccountObject invalidAccountInfo;

        //--------------------------------------Valid listing objects for testing------------------------------------------------//

        validAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "H0H 0H0","NT","santa@northpole.ca", "SantaBaby#112Aap20&");
        validationObject = accountService.validate(validAccountInfo);
        assertTrue("Form was incorrectly validated", validationObject.allValid());

        validAccountInfo = new BuyerAccountObject("ignored","Mary", "Jane",
                "420 Legalnow", "R3V 2X4","MB","maryjane@high.ca", "12Aap20&");
        validationObject = accountService.validate(validAccountInfo);
        assertTrue("Form was incorrectly validated", validationObject.allValid());

        //--------------------------------------Invalid listing objects for testing-----------------------------------------------//

        //================================================Invalid due to null=====================================================//
        invalidAccountInfo = new BuyerAccountObject("ignored",null, "Claus",
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidFirstName());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", null,
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidLastName());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Clause",
                null, "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidStreetAddress());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Clause",
                "123 North Pole", null,"NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPostalCode());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "HOH 0H0",null,"santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidProvince());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "HOH 0H0","NT",null, "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidEmail());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", null);
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPassword());
    }

    @Test
    public void createSeller_verifiesValidationObjectCreation(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);
        SellerAccountValidationObject validationObject;
        SellerAccountObject validAccountInfo;
        SellerAccountObject invalidAccountInfo;

        //--------------------------------------Valid listing objects for testing------------------------------------------------//

        validAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "H0H 0H0","NT","santa@northpole.ca", "SantaBaby#112Aap20&");
        validationObject = accountService.validate(validAccountInfo);
        assertTrue("Form was incorrectly validated", validationObject.allValid());

        validAccountInfo = new SellerAccountObject("ignored","MaryJane",
                "420 Legalnow", "R3V 2X4","MB","maryjane@high.ca", "12Aap20&");
        validationObject = accountService.validate(validAccountInfo);
        assertTrue("Form was incorrectly validated", validationObject.allValid());

        //--------------------------------------Invalid listing objects for testing-----------------------------------------------//

        //================================================Invalid due to null=====================================================//
        invalidAccountInfo = new SellerAccountObject("ignored",null,
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidOrganizationName());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                null, "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidStreetAddress());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", null,"NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPostalCode());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "HOH 0H0",null,"santa@northpole.ca", "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidProvince());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "HOH 0H0","NT",null, "Santa1!");
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidEmail());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", null);
        validationObject = accountService.validate(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPassword());
    }

}

