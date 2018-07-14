package com.ctrlaltelite.copshop.tests.unit;

import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ListingModelTests {

    @Test
    public void createNew_addsListingAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);

        ListingObject listing = new ListingObject("ignored","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "sellerId");

        // Create the listings
        String id1 = listingModel.createNew(listing);
        String id2 = listingModel.createNew(listing);
        String id3 = listingModel.createNew(listing);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Listings", id1));
        assertTrue("Row was not created", database.rowExists("Listings", id2));
        assertTrue("Row was not created", database.rowExists("Listings", id3));
    }

    @Test
    public void delete_deletesListing() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);

        ListingObject listing = new ListingObject("ignored","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "sellerId");

        // Create the listings
        String id1 = listingModel.createNew(listing);
        String id2 = listingModel.createNew(listing);
        String id3 = listingModel.createNew(listing);

        // Delete a listing
        listingModel.delete(id2);

        // Verify the right one was deleted
        assertTrue("Wrong row was deleted", database.rowExists("Listings", id1));
        assertFalse("Row was not deleted", database.rowExists("Listings", id2));
        assertTrue("Wrong row was deleted", database.rowExists("Listings", id3));

        // Delete the rest
        listingModel.delete(id1);
        listingModel.delete(id3);

        // Verify they are all deleted
        assertFalse("Row was not deleted", database.rowExists("Listings", id1));
        assertFalse("Row was not deleted", database.rowExists("Listings", id2));
        assertFalse("Row was not deleted", database.rowExists("Listings", id3));
    }

    @Test
    public void update_updatesListing() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);

        ListingObject listing = new ListingObject("ignored","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "sellerId");

        // Create the listings
        String id1 = listingModel.createNew(listing);
        String id2 = listingModel.createNew(listing);
        String id3 = listingModel.createNew(listing);

        // Update a listing
        ListingObject updatedListing = new ListingObject("ignored","updated-title", "updated-description", "updated-initPrice", "updated-minBid", "updated-auctionStartDate", "updated-auctionEndDate", "updated-category", "updated-sellerId");
        assertTrue("Did not get success back from update", listingModel.update(id2, updatedListing));

        // Verify it updated the correct listing
        assertEquals("Listing title was not updated", "updated-title", database.fetchColumn("Listings", id2, "title"));
        assertEquals("Listing description was not updated", "updated-description", database.fetchColumn("Listings", id2, "description"));

        assertEquals("Wrong listing title updated", "title", database.fetchColumn("Listings", id1, "title"));
        assertEquals("Wrong listing description updated", "description", database.fetchColumn("Listings", id1, "description"));

        assertEquals("Wrong listing title updated", "title", database.fetchColumn("Listings", id3, "title"));
        assertEquals("Wrong listing description updated", "description", database.fetchColumn("Listings", id3, "description"));
    }

    @Test
    public void fetch_fetchesCorrectListing() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);

        ListingObject listing1 = new ListingObject("ignored","t1", "d1", "init1", "min1", "asd1", "aed1", "cat1", "sellerId");
        ListingObject listing2 = new ListingObject("ignored","t2", "d2", "init2", "min2", "asd2", "aed2", "cat2", "sellerId");
        ListingObject listing3 = new ListingObject("ignored","t3", "d3", "init3", "min3", "asd3", "aed3", "cat3", "sellerId");

        // Create the listings
        String id1 = listingModel.createNew(listing1);
        String id2 = listingModel.createNew(listing2);
        String id3 = listingModel.createNew(listing3);

        // Verify correct listings fetched
        assertEquals("Wrong listing fetched", "t1", listingModel.fetch(id1).getTitle());
        assertEquals("Wrong listing fetched", "t2", listingModel.fetch(id2).getTitle());
        assertEquals("Wrong listing fetched", "t3", listingModel.fetch(id3).getTitle());
    }

    @Test
    public void fetchAll_fetchesAllListings() {
        IDatabase database = new MockDatabaseStub();
        IListingModel listingModel = new ListingModel(database);

        ListingObject listing1 = new ListingObject("ignored","t1", "d1", "init1", "min1", "asd1", "aed1", "cat1", "sellerId");
        ListingObject listing2 = new ListingObject("ignored","t2", "d2", "init2", "min2", "asd2", "aed2", "cat2", "sellerId");
        ListingObject listing3 = new ListingObject("ignored","t3", "d3", "init3", "min3", "asd3", "aed3", "cat3", "sellerId");

        // Create the listings
        String id1 = listingModel.createNew(listing1);
        String id2 = listingModel.createNew(listing2);
        String id3 = listingModel.createNew(listing3);

        // Fetch listings and ensure all are in list
        List<ListingObject> list = listingModel.fetchAll();
        assertEquals("Fetched too many listings", 3, list.size());
        assertEquals("Wrong listing fetched", "t1", list.get(2).getTitle());
        assertEquals("Wrong listing fetched", "t2", list.get(1).getTitle());
        assertEquals("Wrong listing fetched", "t3", list.get(0).getTitle());
    }
}
