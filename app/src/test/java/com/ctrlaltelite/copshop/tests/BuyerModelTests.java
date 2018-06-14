package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.BuyerModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuyerModelTests {
    @Test
    public void createNew_addsAccountAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);

        BuyerAccountObject account = new BuyerAccountObject("ignored","name", "other",
                "123 Someplace", "h0h 0h0","MB","email", "pass");

        // Create the accounts
        String id1 = buyerModel.createNew(account);
        String id2 = buyerModel.createNew(account);
        String id3 = buyerModel.createNew(account);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Buyers", id1));
        assertTrue("Row was not created", database.rowExists("Buyers", id2));
        assertTrue("Row was not created", database.rowExists("Buyers", id3));
    }

    @Test
    public void update_updatesAccount() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);

        BuyerAccountObject account = new BuyerAccountObject("ignored","name", "other",
                "123 Someplace", "h0h 0h0","MB","email", "pass");

        // Create the accounts
        String id1 = buyerModel.createNew(account);
        String id2 = buyerModel.createNew(account);
        String id3 = buyerModel.createNew(account);

        // Update a account
        BuyerAccountObject updatedAccount = new BuyerAccountObject("ignored","updated-name", "other",
                "123 Someplace", "h0h 0h0","MB","updated-email", "updated-pass");

        assertTrue("Did not get success back from update", buyerModel.update(id2, updatedAccount));

        // Verify it updated the correct account
        assertEquals("Account username was not updated", "updated-name", database.fetchColumn("Buyers", id2, "username"));
        assertEquals("Account password was not updated", "updated-pass", database.fetchColumn("Buyers", id2, "password"));
        assertEquals("Account email was not updated", "updated-email", database.fetchColumn("Buyers", id2, "email"));

        assertEquals("Wrong account username updated", "name", database.fetchColumn("Buyers", id1, "username"));
        assertEquals("Wrong account password updated", "pass", database.fetchColumn("Buyers", id1, "password"));
        assertEquals("Wrong account password updated", "email", database.fetchColumn("Buyers", id1, "email"));

        assertEquals("Wrong account username updated", "name", database.fetchColumn("Buyers", id3, "username"));
        assertEquals("Wrong account password updated", "pass", database.fetchColumn("Buyers", id3, "password"));
        assertEquals("Wrong account password updated", "email", database.fetchColumn("Buyers", id3, "email"));
    }

    @Test
    public void fetch_fetchesCorrectAccount() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");
        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name3", "other",
                "123 Someplace", "h0h 0h0","MB","email3", "pass3");

        // Create the accounts
        String id1 = buyerModel.createNew(account1);
        String id2 = buyerModel.createNew(account2);
        String id3 = buyerModel.createNew(account3);

        // Verify correct accounts fetched
        assertEquals("Wrong account fetched", "name1", buyerModel.fetch(id1).getUsername());
        assertEquals("Wrong account fetched", "name2", buyerModel.fetch(id2).getUsername());
        assertEquals("Wrong account fetched", "name3", buyerModel.fetch(id3).getUsername());
    }

    @Test
    public void findByUsername_findsCorrectUser() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");
        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name3", "other",
                "123 Someplace", "h0h 0h0","MB","email3", "pass3");

        // Create the accounts
        String id1 = buyerModel.createNew(account1);
        String id2 = buyerModel.createNew(account2);
        String id3 = buyerModel.createNew(account3);

        // Verify the correct user is found
        assertEquals("Did not find correct user", "name2", buyerModel.findByEmail("email2").getUsername());
        assertEquals("Did not find correct user", "name1", buyerModel.findByEmail("email1").getUsername());
        assertEquals("Did not find correct user", "name3", buyerModel.findByEmail("email3").getUsername());
        assertFalse("Found nonexistent user", null != buyerModel.findByEmail("email4"));
    }

    @Test
    public void checkUsernamePasswordMatch_verifiesLoginProperly() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "other",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");
        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name3", "other",
                "123 Someplace", "h0h 0h0","MB","email3", "pass3");

        // Create the accounts
        String id1 = buyerModel.createNew(account1);
        String id2 = buyerModel.createNew(account2);
        String id3 = buyerModel.createNew(account3);

        // Check credential pairs
        assertTrue("Did not verify correct credentials", buyerModel.checkEmailPasswordMatch("email1", "pass1"));
        assertTrue("Did not verify correct credentials", buyerModel.checkEmailPasswordMatch("email2", "pass2"));
        assertTrue("Did not verify correct credentials", buyerModel.checkEmailPasswordMatch("email3", "pass3"));
        assertFalse("Verified false credentials", buyerModel.checkEmailPasswordMatch("email1", "pass3"));
    }
}
