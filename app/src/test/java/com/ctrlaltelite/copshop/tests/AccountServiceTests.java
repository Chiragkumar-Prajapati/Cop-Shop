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
    public void registerNewBuyer_validatesAndSavesBuyer(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject account1 = new BuyerAccountObject("","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1@email.com", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2@email.com", "pass2");

        // Save the new listings
        BuyerAccountValidationObject bav1 = accountService.registerNewBuyer(account1);
        BuyerAccountValidationObject bav2 = accountService.registerNewBuyer(account2);
        BuyerAccountValidationObject bav3 = accountService.registerNewBuyer(account1);

        // Verify they were validated correctly
        assertTrue("Account was incorrectly flagged as invalid", bav1.allValid());
        assertTrue("Account was incorrectly flagged as invalid", bav2.allValid());
        assertFalse("Account email was incorrectly flagged as valid", bav3.getValidEmail());
        assertFalse("Account was incorrectly flagged as valid", bav3.allValid());

        // Verify they were created
        assertTrue("Account was not created", database.rowExists("Buyers", "0"));
        assertTrue("Account was not created", database.rowExists("Buyers", "1"));
        assertFalse("Duplicate account was created", database.rowExists("Buyers", "2"));
    }

    @Test
    public void registerNewSeller_validatesAndSavesSeller(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        SellerAccountObject account1 = new SellerAccountObject("ignored","sel1",
                "123 Someplace", "h0h 0h0","MB","email1@email.com", "pass1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","sel2",
                "123 Someplace", "h0h 0h0","MB","email2@email.com", "pass2");

        // Save the new listings
        SellerAccountValidationObject sav1 = accountService.registerNewSeller(account1);
        SellerAccountValidationObject sav2 = accountService.registerNewSeller(account2);
        SellerAccountValidationObject sav3 = accountService.registerNewSeller(account1);

        // Verify they were validated correctly
        assertTrue("Account was incorrectly flagged as invalid", sav1.allValid());
        assertTrue("Account was incorrectly flagged as invalid", sav2.allValid());
        assertFalse("Account email was incorrectly flagged as valid", sav3.getValidEmail());
        assertFalse("Account was incorrectly flagged as valid", sav3.allValid());

        // Verify they were created
        assertTrue("Account was not created", database.rowExists("Sellers", "0"));
        assertTrue("Account was not created", database.rowExists("Sellers", "1"));
        assertFalse("Duplicate account was created", database.rowExists("Sellers", "2"));

    }

    @Test
    public void updateBuyer_savesAccountChanges(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1@email.com", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2@email.com", "pass2");

        // Save the new listings
        BuyerAccountValidationObject bav1 = accountService.registerNewBuyer(account1);
        BuyerAccountValidationObject bav2 = accountService.registerNewBuyer(account2);

        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email3@email.com", "pass1");

        BuyerAccountValidationObject bav3 = accountService.updateBuyerAccount("0", account3);
        assertTrue("Row was not updated", bav3.allValid());

        // Verify they were updated
        assertNull("Row with old email exists", accountService.fetchAccountByEmail("email1@email.com"));
        assertNotNull("Row with new email does not exist", accountService.fetchAccountByEmail("email3@email.com"));
    }

    @Test
    public void updateSeller_savesAccountChanges(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        SellerAccountObject account1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1@email.com", "pass1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2",
                "123 Someplace", "h0h 0h0","MB","email2@email.com", "pass2");

        // Save the new listings
        SellerAccountValidationObject sav1 = accountService.registerNewSeller(account1);
        SellerAccountValidationObject sav2 = accountService.registerNewSeller(account2);

        assertTrue("Row was not created", sav1.allValid());
        assertTrue("Row was not created", sav2.allValid());

        SellerAccountObject account3 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email3@email.com", "pass1");

        SellerAccountValidationObject sav3 = accountService.updateSellerAccount("0", account3);
        assertTrue("Row was not updated", sav3.allValid());

        // Verify they were updated
        assertNotNull("Row with new email does not exist", accountService.fetchAccountByEmail("email3@email.com"));
        assertNull("Row with old email exists", accountService.fetchAccountByEmail("email1@email.com"));

    }

    @Test
    public void createBuyer_verifiesValidationObjectCreation_validObjects() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        BuyerAccountObject b1 = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "H0H 0H0","NT","santa@northpole.ca", "SantaBaby#112Aap20&");
        BuyerAccountValidationObject bav1 = accountService.registerNewBuyer(b1);
        assertTrue("Form was incorrectly invalidated", bav1.allValid());

        BuyerAccountObject b2 = new BuyerAccountObject("ignored","Mary", "Jane",
                "420 Legalnow", "R3V 2X4","MB","maryjane@high.ca", "12Aap20&");
        BuyerAccountValidationObject bav2 = accountService.registerNewBuyer(b2);
        assertTrue("Form was incorrectly invalidated", bav2.allValid());
    }

    @Test
    public void createSeller_verifiesValidationObjectCreation_validObjects() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);

        SellerAccountObject s1 = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "H0H 0H0","NT","santa@northpole.ca", "SantaBaby#112Aap20&");
        SellerAccountValidationObject sav1 = accountService.registerNewSeller(s1);
        assertTrue("Form was incorrectly invalidated", sav1.allValid());

        SellerAccountObject s2 = new SellerAccountObject("ignored","MaryJane",
                "420 Legalnow", "R3V 2X4","MB","maryjane@high.ca", "12Aap20&");
        SellerAccountValidationObject sav2 = accountService.registerNewSeller(s2);
        assertTrue("Form was incorrectly invalidated", sav2.allValid());
    }

    @Test
    public void createBuyer_verifiesValidationObjectCreation_nullObjects(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);
        BuyerAccountValidationObject validationObject;
        BuyerAccountObject invalidAccountInfo;

        //================================================Invalid due to null=====================================================//
        invalidAccountInfo = new BuyerAccountObject("ignored",null, "Claus",
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidFirstName());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", null,
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidLastName());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Clause",
                null, "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidStreetAddress());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Clause",
                "123 North Pole", null,"NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPostalCode());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "HOH 0H0",null,"santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidProvince());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "HOH 0H0","NT",null, "Santa1!");
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidEmail());

        invalidAccountInfo = new BuyerAccountObject("ignored","Santa", "Claus",
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", null);
        validationObject = accountService.registerNewBuyer(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPassword());
    }

    @Test
    public void createSeller_verifiesValidationObjectCreation_nullObjects(){
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IBuyerModel buyerModel = new BuyerModel(database);
        IAccountService accountService = new AccountService(sellerModel, buyerModel);
        SellerAccountValidationObject validationObject;
        SellerAccountObject validAccountInfo;
        SellerAccountObject invalidAccountInfo;

        //--------------------------------------Invalid listing objects for testing-----------------------------------------------//

        //================================================Invalid due to null=====================================================//
        invalidAccountInfo = new SellerAccountObject("ignored",null,
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewSeller(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidOrganizationName());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                null, "HOH 0H0","NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewSeller(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidStreetAddress());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", null,"NT","santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewSeller(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPostalCode());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "HOH 0H0",null,"santa@northpole.ca", "Santa1!");
        validationObject = accountService.registerNewSeller(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidProvince());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "HOH 0H0","NT",null, "Santa1!");
        validationObject = accountService.registerNewSeller(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidEmail());

        invalidAccountInfo = new SellerAccountObject("ignored","Santa",
                "123 North Pole", "HOH 0H0","NT","santa@northpole.ca", null);
        validationObject = accountService.registerNewSeller(invalidAccountInfo);
        assertFalse("Form was incorrectly validated", validationObject.allValid());
        assertFalse("Expected invalid field was valid", validationObject.getValidPassword());
    }

    @Test
    public void getBuyerName_getsCorrectName() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);
        ISellerModel sellerModel = new SellerModel(database);
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

        assertEquals("", "name1", accountService.getBuyerName(id1));
        assertEquals("", "name2", accountService.getBuyerName(id2));
        assertEquals("", "name3", accountService.getBuyerName(id3));
    }

}

