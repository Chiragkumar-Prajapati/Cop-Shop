package com.ctrlaltelite.copshop.tests.unit;

import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.BidModel;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BidModelTests {

    @Test
    public void createNew_addsNewBidAndReturnsId() {
        IDatabase database = new MockDatabaseStub();
        IBidModel bidModel = new BidModel(database);

        BidObject bid = new BidObject("", "0", "0", "1.00");

        // Create the bids
        String id1 = bidModel.createNew(bid);
        String id2 = bidModel.createNew(bid);
        String id3 = bidModel.createNew(bid);

        // Verify they were created
        assertTrue("Row was not created", database.rowExists("Bids", id1));
        assertTrue("Row was not created", database.rowExists("Bids", id2));
        assertTrue("Row was not created", database.rowExists("Bids", id3));
    }

    @Test
    public void fetch_fetchesCorrectBid() {
        IDatabase database = new MockDatabaseStub();
        IBidModel bidModel = new BidModel(database);

        BidObject bid1 = new BidObject("", "0", "0", "1.00");
        BidObject bid2 = new BidObject("", "0", "0", "2.00");
        BidObject bid3 = new BidObject("", "0", "0", "3.00");

        // Create the bids
        String id1 = bidModel.createNew(bid1);
        String id2 = bidModel.createNew(bid2);
        String id3 = bidModel.createNew(bid3);

        // Verify correct bids fetched
        assertEquals("Wrong bid fetched", "1.00", bidModel.fetch(id1).getBidAmt());
        assertEquals("Wrong bid fetched", "2.00", bidModel.fetch(id2).getBidAmt());
        assertEquals("Wrong bid fetched", "3.00", bidModel.fetch(id3).getBidAmt());
    }

    @Test
    public void findAllByListing_findsAllCorrectBids() {
        IDatabase database = new MockDatabaseStub();
        IBidModel bidModel = new BidModel(database);
        IListingModel listingModel = new ListingModel(database);

        BidObject bid1 = new BidObject("", "0", "0", "1.00");
        BidObject bid2 = new BidObject("", "1", "0", "2.00");
        BidObject bid3 = new BidObject("", "1", "0", "3.00");
        ListingObject listing1 = new ListingObject("","t1", "d1", "init1", "min1", "asd1", "aed1", "cat1","sellerId");
        ListingObject listing2 = new ListingObject("","t2", "d2", "init2", "min2", "asd2", "aed2", "cat2","sellerId");
        ListingObject listing3 = new ListingObject("","t3", "d3", "init3", "min3", "asd3", "aed3", "cat3","sellerId");

        // Create the listings
        String lId1 = listingModel.createNew(listing1);
        String lId2 = listingModel.createNew(listing2);
        String lId3 = listingModel.createNew(listing3);

        // Create the bids
        String bId1 = bidModel.createNew(bid1);
        String bId2 = bidModel.createNew(bid2);
        String bId3 = bidModel.createNew(bid3);

        List<BidObject> bids = bidModel.findAllByListing("1");
        assertEquals("Wrong number of bids fetched", 2, bids.size());
        assertEquals("Wrong bid fetched", "3.00", bids.get(0).getBidAmt());
        assertEquals("Wrong bid fetched", "2.00", bids.get(1).getBidAmt());
        bids = bidModel.findAllByListing("0");
        assertEquals("Wrong number of bids fetched", 1, bids.size());
        assertEquals("Wrong bid fetched", "1.00", bids.get(0).getBidAmt());
        bids = bidModel.findAllByListing("2");
        assertEquals("Wrong number of bids fetched", 0, bids.size());
    }
}
