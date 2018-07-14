package com.ctrlaltelite.copshop.tests.integration;

import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.IBidService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.BidService;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.hsqldb.BidModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.hsqldb.BuyerModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.hsqldb.ListingModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.hsqldb.SellerModelHSQLDB;
import com.ctrlaltelite.copshop.tests.integration.db.HSQLDBTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class BidServiceIntegrationTests {
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
    public void fetchBidsForListing_fetchesAllBids() {
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidService bidService = new BidService(bidModel, buyerModel, listingModel);

        SellerAccountObject sAccount1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        SellerAccountObject sAccount2 = new SellerAccountObject("ignored","name2",
                "123 Someplace", "h0h 0h0","MB","email2", "pass2");
        String sId1 = sellerModel.createNew(sAccount1);
        String sId2 = sellerModel.createNew(sAccount2);
        assertNotNull("Seller was not created", sId1);
        assertNotNull("Seller was not created", sId2);

        BuyerAccountObject bAccount1 = new BuyerAccountObject("","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email3", "pass1");
        String bId1 = buyerModel.createNew(bAccount1);
        assertNotNull("Buyer was not created", bId1);

        // Create listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate","category", sId1);
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate","category", sId2);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        assertNotNull("Listing was not created", lId1);
        assertNotNull("Listing was not created", lId2);

        // Create bids
        BidObject bid1 = new BidObject("", lId1, bId1, "1.00");
        BidObject bid2 = new BidObject("", lId2, bId1, "2.00");
        BidObject bid3 = new BidObject("", lId2, bId1, "3.00");
        String biId1 = bidModel.createNew(bid1);
        String biId2 = bidModel.createNew(bid2);
        String biId3 = bidModel.createNew(bid3);

        List bids = bidService.fetchBidsForListing(lId2);
        assertEquals("Did not fetch correct number of bids", 2, bids.size());
        bids = bidService.fetchBidsForListing(lId1);
        assertEquals("Did not fetch correct number of bids", 1, bids.size());
    }

    @Test
    public void createBid_createsBidsProperly() {
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidService bidService = new BidService(bidModel, buyerModel, listingModel);

        SellerAccountObject sAccount1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String sId1 = sellerModel.createNew(sAccount1);
        assertNotNull("Buyer was not created", sId1);

        BuyerAccountObject bAccount1 = new BuyerAccountObject("","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String bId1 = buyerModel.createNew(bAccount1);
        assertNotNull("Buyer was not created", bId1);

        // Create listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate","category", sId1);
        String lId1 = listingModel.createNew(l1);
        assertNotNull("Listing was not created", lId1);

        assertTrue("Failed to create bid", bidService.createBid("5.00",lId1,"6.00",bId1));
        assertTrue("Failed to create bid", bidService.createBid("6.00",lId1,"6.00",bId1));
        assertTrue("Failed to create bid", bidService.createBid("22.00",lId1,"22.00",bId1));
        assertFalse("Created invalid bid", bidService.createBid("2.00",lId1,"1.00",bId1));
        assertFalse("Created invalid bid", bidService.createBid("33.00",lId1,"32.99",bId1));
        assertFalse("Created invalid bid", bidService.createBid("5.00",lId1,"4.00",bId1));
    }
}
