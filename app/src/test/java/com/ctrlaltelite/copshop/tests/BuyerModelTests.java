package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.logic.interfaces.IBuyerModel;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.models.BuyerModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuyerModelTests {
    @Test
    public void createNew_addsAccountAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel sellerModel = new BuyerModel(database);

        BuyerAccountObject account = new BuyerAccountObject("ignored","name", "pass", "email");

        // Create the accounts
        String id1 = sellerModel.createNew(account);
        String id2 = sellerModel.createNew(account);
        String id3 = sellerModel.createNew(account);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Buyers", id1));
        assertTrue("Row was not created", database.rowExists("Buyers", id2));
        assertTrue("Row was not created", database.rowExists("Buyers", id3));
    }

    @Test
    public void update_updatesAccount() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel sellerModel = new BuyerModel(database);

        BuyerAccountObject account = new BuyerAccountObject("ignored","name", "pass", "email");

        // Create the accounts
        String id1 = sellerModel.createNew(account);
        String id2 = sellerModel.createNew(account);
        String id3 = sellerModel.createNew(account);

        // Update a account
        BuyerAccountObject updatedAccount = new BuyerAccountObject("ignored","updated-name", "updated-pass", "updated-email");
        assertTrue("Did not get success back from update", sellerModel.update(id2, updatedAccount));

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
        IBuyerModel sellerModel = new BuyerModel(database);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "pass1", "email1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "pass2", "email2");
        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name3", "pass3", "email3");

        // Create the accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

        // Verify correct accounts fetched
        assertEquals("Wrong account fetched", "name1", sellerModel.fetch(id1).getUsername());
        assertEquals("Wrong account fetched", "name2", sellerModel.fetch(id2).getUsername());
        assertEquals("Wrong account fetched", "name3", sellerModel.fetch(id3).getUsername());
    }

    @Test
    public void findByUsername_findsCorrectUser() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel sellerModel = new BuyerModel(database);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "pass1", "email1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "pass2", "email2");
        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name3", "pass3", "email3");

        // Create the accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

        // Verify the correct user is found
        assertEquals("Did not find correct user", "name2", sellerModel.findByUsername("name2").getUsername());
        assertEquals("Did not find correct user", "name1", sellerModel.findByUsername("name1").getUsername());
        assertEquals("Did not find correct user", "name3", sellerModel.findByUsername("name3").getUsername());
        assertFalse("Found nonexistent user", null != sellerModel.findByUsername("name4"));
    }

    @Test
    public void checkUsernamePasswordMatch_verifiesLoginProperly() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel sellerModel = new BuyerModel(database);

        BuyerAccountObject account1 = new BuyerAccountObject("ignored","name1", "pass1", "email1");
        BuyerAccountObject account2 = new BuyerAccountObject("ignored","name2", "pass2", "email2");
        BuyerAccountObject account3 = new BuyerAccountObject("ignored","name3", "pass3", "email3");

        // Create the accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

        // Check credential pairs
        assertTrue("Did not verify correct credentials", sellerModel.checkUsernamePasswordMatch("name1", "pass1"));
        assertTrue("Did not verify correct credentials", sellerModel.checkUsernamePasswordMatch("name2", "pass2"));
        assertTrue("Did not verify correct credentials", sellerModel.checkUsernamePasswordMatch("name3", "pass3"));
        assertFalse("Verified false credentials", sellerModel.checkUsernamePasswordMatch("name1", "pass3"));
    }
}
