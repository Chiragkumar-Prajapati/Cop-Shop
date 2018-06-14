package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.SellerModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class SellerModelTests {
    @Test
    public void createNew_addsAccountAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account = new SellerAccountObject("ignored", "pass", "email", "name");

        // Create the accounts
        String id1 = sellerModel.createNew(account);
        String id2 = sellerModel.createNew(account);
        String id3 = sellerModel.createNew(account);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Sellers", id1));
        assertTrue("Row was not created", database.rowExists("Sellers", id2));
        assertTrue("Row was not created", database.rowExists("Sellers", id3));
    }

    @Test
    public void update_updatesAccount() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account = new SellerAccountObject("ignored", "pass", "email", "name");

        // Create the accounts
        String id1 = sellerModel.createNew(account);
        String id2 = sellerModel.createNew(account);
        String id3 = sellerModel.createNew(account);

        // Update a account
        SellerAccountObject updatedAccount = new SellerAccountObject("ignored", "updated-pass", "updated-email", "updated-name");
        assertTrue("Did not get success back from update", sellerModel.update(id2, updatedAccount));

        // Verify it updated the correct account
        assertEquals("Account name was not updated", "updated-name", database.fetchColumn("Sellers", id2, "name"));
        assertEquals("Account password was not updated", "updated-pass", database.fetchColumn("Sellers", id2, "password"));
        assertEquals("Account email was not updated", "updated-email", database.fetchColumn("Sellers", id2, "email"));

        assertEquals("Wrong account name updated", "name", database.fetchColumn("Sellers", id1, "name"));
        assertEquals("Wrong account password updated", "pass", database.fetchColumn("Sellers", id1, "password"));
        assertEquals("Wrong account email updated", "email", database.fetchColumn("Sellers", id1, "email"));

        assertEquals("Wrong account name updated", "name", database.fetchColumn("Sellers", id3, "name"));
        assertEquals("Wrong account password updated", "pass", database.fetchColumn("Sellers", id3, "password"));
        assertEquals("Wrong account email updated", "email", database.fetchColumn("Sellers", id3, "email"));
    }

    @Test
    public void fetch_fetchesCorrectAccount() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account1 = new SellerAccountObject("ignored", "pass1", "email1", "name1");
        SellerAccountObject account2 = new SellerAccountObject("ignored", "pass2", "email2", "name2");
        SellerAccountObject account3 = new SellerAccountObject("ignored", "pass3", "email3", "name3");

        // Create the accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

        // Verify correct accounts fetched
        assertEquals("Wrong account fetched", "email1", sellerModel.fetch(id1).getEmail());
        assertEquals("Wrong account fetched", "email2", sellerModel.fetch(id2).getEmail());
        assertEquals("Wrong account fetched", "email3", sellerModel.fetch(id3).getEmail());
    }

    @Test
    public void findByEmail_findsCorrectUser() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account1 = new SellerAccountObject("ignored", "pass1", "email1", "name1");
        SellerAccountObject account2 = new SellerAccountObject("ignored", "pass2", "email2", "name2");
        SellerAccountObject account3 = new SellerAccountObject("ignored", "pass3", "email3", "name3");

        // Create the accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

        // Verify the correct user is found
        assertEquals("Did not find correct user", "email2", sellerModel.findByEmail("email2").getEmail());
        assertEquals("Did not find correct user", "email1", sellerModel.findByEmail("email1").getEmail());
        assertEquals("Did not find correct user", "email3", sellerModel.findByEmail("email3").getEmail());
        assertFalse("Found nonexistent user", null != sellerModel.findByEmail("email4"));
    }
}
