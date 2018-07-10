package com.ctrlaltelite.copshop.tests.integration;

import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.objects.BuyerAccountValidationObject;
import com.ctrlaltelite.copshop.objects.SellerAccountValidationObject;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.AccountObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.stubs.hsqldb.BuyerModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.stubs.hsqldb.SellerModelHSQLDB;
import com.ctrlaltelite.copshop.tests.integration.db.HSQLDBTestUtil;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

public class AccountServiceIntegrationTests {
    private HSQLDBTestUtil dbUtil =  new HSQLDBTestUtil();

    @Before
    public void setup() {
        dbUtil.setup();
    }

    @After
    public void teardown() {
        dbUtil.reset();
    }

    @Test
    public void validateUsernameAndPassword_verifiesLoginProperly() {
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        // Create an account
        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String id1 = buyerModel.createNew(account1);

        assertNotNull("Account was not created", id1);

        // Try pulling some accounts, given some creds
        AccountObject buyer1 = accountService.validateEmailAndPassword("email1", "pass1");
        AccountObject buyer2 = accountService.validateEmailAndPassword("emailWrong", "pass1");
        AccountObject buyer3 = accountService.validateEmailAndPassword("email1", "passWrong");

        // Check credential pairs
        assertTrue("Did not validate correct credentials", buyer1 != null);
        assertTrue("Returned wrong object type, got " + buyer1.getClass().toString(), buyer1 instanceof BuyerAccountObject);
        assertTrue("Verified false credentials", buyer2 == null);
        assertTrue("Verified false credentials", buyer3 == null);

    }

    @Test
    public void registerNewBuyer_savesBuyerAndReturnsID(){
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject account1 = new BuyerAccountObject("","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");

        // Save the new listings
        String id1 = accountService.registerNewBuyer(account1);
        String id2 = accountService.registerNewBuyer(account2);
        String id3 = accountService.registerNewBuyer(account1);

        // Verify they were created
        assertNotNull("Account was not created", id1);
        assertNotNull("Account was not created", id2);
        assertNull("Account with duplicate email created", id3);
    }

    @Test
    public void registerNewSeller_savesSellerAndReturnsID(){
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        SellerAccountObject account1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");

        // Save the new listings
        String id1 = accountService.registerNewSeller(account1);
        String id2 = accountService.registerNewSeller(account2);
        String id3 = accountService.registerNewSeller(account1);

        // Verify id was returned
        assertNotNull("Account was not created", id1);
        assertNotNull("Account was not created", id2);
        assertNull("Account with duplicate email created", id3);

        // Verify they were created
        assertTrue("Account was not created", null != sellerModel.fetch(id1));
        assertTrue("Account was not created", null != sellerModel.fetch(id2));
    }

    @Test
    public void updateBuyer_savesAccountChanges(){
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");

        // Save the new listings
        String id1 = accountService.registerNewBuyer(account1);
        String id2 = accountService.registerNewBuyer(account2);

        assertNotNull("Account was not created", id1);
        assertNotNull("Account was not created", id2);

        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email3", "pass1");

        boolean success = accountService.updateBuyerAccount(id1, account3);
        assertTrue("Row was not updated", success);

        // Verify they were updated
        assertNull("Row with old email exists", accountService.fetchAccountByEmail("email1"));
        assertNotNull("Row with new email does not exist", accountService.fetchAccountByEmail("email3"));
    }

    @Test
    public void updateSeller_savesAccountChanges(){
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        SellerAccountObject account1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");

        // Save the new listings
        String id1 = accountService.registerNewSeller(account1);
        String id2 = accountService.registerNewSeller(account2);

        assertNotNull("Account was not created", id1);
        assertNotNull("Account was not created", id2);

        SellerAccountObject account3 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email3", "pass1");

        boolean success = accountService.updateSellerAccount(id1, account3);
        assertTrue("Row was not updated", success);

        // Verify they were updated
        assertNull("Row with old email exists", accountService.fetchAccountByEmail("email1"));
        assertNotNull("Row with new email does not exist", accountService.fetchAccountByEmail("email3"));
    }

    @Test
    public void createBuyer_verifiesValidationObjectCreation(){
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
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
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
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

    @Test
    public void getBuyerName_getsCorrectName() {
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject account1 = new BuyerAccountObject("","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");
        BuyerAccountObject account3 = new BuyerAccountObject("","name3", "other",
                "123 Someplace", "h0h 0h0","MB","email3", "pass3");

        // Create the accounts
        String id1 = buyerModel.createNew(account1);
        String id2 = buyerModel.createNew(account2);
        String id3 = buyerModel.createNew(account3);

        assertNotNull("Account was not created", id1);
        assertNotNull("Account was not created", id2);
        assertNotNull("Account was not created", id3);

        assertEquals("", "name1", accountService.getBuyerName(id1));
        assertEquals("", "name2", accountService.getBuyerName(id2));
        assertEquals("", "name3", accountService.getBuyerName(id3));
    }

}

