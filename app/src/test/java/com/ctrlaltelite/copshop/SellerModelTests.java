package com.ctrlaltelite.copshop;

import com.ctrlaltelite.copshop.logic.interfaces.ISellerModel;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.models.SellerModel;
import org.junit.Test;

import static org.junit.Assert.*;

public class SellerModelTests {
    @Test
    public void createNew_addsAccountAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account = new SellerAccountObject("ignored","name", "pass", "email");

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

        SellerAccountObject account = new SellerAccountObject("ignored","name", "pass", "email");

        // Create the accounts
        String id1 = sellerModel.createNew(account);
        String id2 = sellerModel.createNew(account);
        String id3 = sellerModel.createNew(account);

        // Update a account
        SellerAccountObject updatedAccount = new SellerAccountObject("ignored","updated-name", "updated-pass", "updated-email");
        assertTrue("Did not get success back from update", sellerModel.update(id2, updatedAccount));

        // Verify it updated the correct account
        assertEquals("Account username was not updated", "updated-name", database.fetchColumn("Sellers", id2, "username"));
        assertEquals("Account password was not updated", "updated-pass", database.fetchColumn("Sellers", id2, "password"));
        assertEquals("Account email was not updated", "updated-email", database.fetchColumn("Sellers", id2, "email"));

        assertEquals("Wrong account username updated", "name", database.fetchColumn("Sellers", id1, "username"));
        assertEquals("Wrong account password updated", "pass", database.fetchColumn("Sellers", id1, "password"));
        assertEquals("Wrong account password updated", "email", database.fetchColumn("Sellers", id1, "email"));

        assertEquals("Wrong account username updated", "name", database.fetchColumn("Sellers", id3, "username"));
        assertEquals("Wrong account password updated", "pass", database.fetchColumn("Sellers", id3, "password"));
        assertEquals("Wrong account password updated", "email", database.fetchColumn("Sellers", id3, "email"));
    }

    @Test
    public void fetch_fetchesCorrectAccount() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account1 = new SellerAccountObject("ignored","name1", "pass1", "email1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2", "pass2", "email2");
        SellerAccountObject account3 = new SellerAccountObject("ignored","name3", "pass3", "email3");

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
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account1 = new SellerAccountObject("ignored","name1", "pass1", "email1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2", "pass2", "email2");
        SellerAccountObject account3 = new SellerAccountObject("ignored","name3", "pass3", "email3");

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
        ISellerModel sellerModel = new SellerModel(database);

        SellerAccountObject account1 = new SellerAccountObject("ignored","name1", "pass1", "email1");
        SellerAccountObject account2 = new SellerAccountObject("ignored","name2", "pass2", "email2");
        SellerAccountObject account3 = new SellerAccountObject("ignored","name3", "pass3", "email3");

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
