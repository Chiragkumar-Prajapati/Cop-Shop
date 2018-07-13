package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.logic.services.IBidService;
import com.ctrlaltelite.copshop.logic.services.stubs.BidService;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.BidModel;
import com.ctrlaltelite.copshop.persistence.stubs.BuyerModel;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BidServiceTests {
    @Test
    public void fetchBidsForListing_fetchesAllBids() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IBidService bidService = new BidService(bidModel, buyerModel, listingModel);

        // Create listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate","category", "",  "3");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate","category", "", "1");
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);

        // Create bids
        BidObject bid1 = new BidObject("", "0", "0", "1.00");
        BidObject bid2 = new BidObject("", "1", "0", "2.00");
        BidObject bid3 = new BidObject("", "1", "0", "3.00");
        String bId1 = bidModel.createNew(bid1);
        String bId2 = bidModel.createNew(bid2);
        String bId3 = bidModel.createNew(bid3);

        List bids = bidService.fetchBidsForListing("1");
        assertEquals("Did not fetch correct number of bids", 2, bids.size());
        bids = bidService.fetchBidsForListing("0");
        assertEquals("Did not fetch correct number of bids", 1, bids.size());
    }

    @Test
    public void createBid_createsBidsProperly() {
        IDatabase database = new MockDatabaseStub();
        IBuyerModel buyerModel = new BuyerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IBidService bidService = new BidService(bidModel, buyerModel, listingModel);

        assertTrue("Failed to create bid", bidService.createBid("5.00","0","6.00","0"));
        assertTrue("Failed to create bid", bidService.createBid("6.00","0","6.00","0"));
        assertTrue("Failed to create bid", bidService.createBid("22.00","0","22.00","0"));
        assertFalse("Created invalid bid", bidService.createBid("2.00","0","1.00","0"));
        assertFalse("Created invalid bid", bidService.createBid("33.00","0","32.99","0"));
        assertFalse("Created invalid bid", bidService.createBid("5.00","0","4.00","0"));
    }
}
