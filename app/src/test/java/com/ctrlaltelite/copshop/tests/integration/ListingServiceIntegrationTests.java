package com.ctrlaltelite.copshop.tests.integration;

import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;
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
import com.ctrlaltelite.copshop.tests.db.HSQLDBTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ListingServiceIntegrationTests {
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
    public void fetchListings_fetchesAllListings() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        SellerAccountObject sAccount1 = new SellerAccountObject("ignored","name1",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String sId1 = sellerModel.createNew(sAccount1);
        assertNotNull("Seller was not created", sId1);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);

        List listings = listingService.fetchListings();
        assertEquals("Did not fetch correct number of listings", 3, listings.size());
    }

    @Test
    public void fetchFilteredListings_fetchesFilteredListings() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        SellerAccountObject account1 = new SellerAccountObject("ignored", "name1", "123 Street", "A1A 1A1", "MB", "email1", "pass1");
        SellerAccountObject account2 = new SellerAccountObject("ignored", "name2", "123 Street", "A1A 1A1", "MB", "email2", "pass2");
        SellerAccountObject account3 = new SellerAccountObject("ignored", "name3", "123 Street", "A1A 1A1", "MB", "email3", "pass2");

        // Create the seller accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

        assertNotNull("Seller was not created", id1);
        assertNotNull("Seller was not created", id1);
        assertNotNull("Seller was not created", id1);

        // Create the listings
        ListingObject l1 = new ListingObject("","title1", "description1", "initPrice", "minBid", "02/02/2020 11:00", "02/02/2025 11:00", "category1", id3);
        ListingObject l2 = new ListingObject("","title2", "description2", "initPrice", "minBid", "02/02/2010 11:00", "02/02/2020 11:00", "category1", id1);
        ListingObject l3 = new ListingObject("","title3", "description3", "initPrice", "minBid", "02/02/2017 11:00", "02/02/2019 11:00", "category2", id2);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);

        // fetch listings with no filter, should fetch all listings
        List listings = listingService.fetchListingsByFilters("","","","");
        assertEquals("Did not fetch correct number of listings", 3, listings.size());

        // fetch listings by name/title
        listings = listingService.fetchListingsByFilters("title1","","","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("title2","","","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("title3","","","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("Non-existent Title","","","");
        assertEquals("Did not fetch correct number of listings", 0, listings.size());

        // fetch listings by category
        listings = listingService.fetchListingsByFilters("","","category1","");
        assertEquals("Did not fetch correct number of listings", 2, listings.size());
        listings = listingService.fetchListingsByFilters("","","category2","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("","","Non-existent Category","");
        assertEquals("Did not fetch correct number of listings", 0, listings.size());

        // fetch listings by status
        listings = listingService.fetchListingsByFilters("","","","Active");
        assertEquals("Did not fetch correct number of listings", 2, listings.size());
        listings = listingService.fetchListingsByFilters("","","","Inactive");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("","","Invalid Status","");
        assertEquals("Did not fetch correct number of listings", 0, listings.size());

        // fetch listings by location
        listings = listingService.fetchListingsByFilters("","name1","","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("","name2","","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("Non-existent Location","","","");
        assertEquals("Did not fetch correct number of listings", 0, listings.size());

        // fetch listings using multiple filters
        listings = listingService.fetchListingsByFilters("title1","","category1","");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("","","category2","active");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("description2","","category1","active");
        assertEquals("Did not fetch correct number of listings", 1, listings.size());
        listings = listingService.fetchListingsByFilters("Non-existent Title","","Non-existent Category","");
        assertEquals("Did not fetch correct number of listings", 0, listings.size());
    }

    @Test
    public void getSellerNameFromListing_findsCorrectSeller() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the accounts
        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId3);
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId2);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);

        String gotName = listingService.getSellerNameFromListing(lId1);
        assertEquals("Got wrong seller name from listing", "three", gotName);
        gotName = listingService.getSellerNameFromListing(lId2);
        assertEquals("Got wrong seller name from listing", "one", gotName);
        gotName = listingService.getSellerNameFromListing(lId3);
        assertEquals("Got wrong seller name from listing", "two", gotName);
    }

    @Test
    public void getSellerName_findsCorrectSeller() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the accounts
        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        assertNotNull("Seller was not created", sId1);
        assertNotNull("Seller was not created", sId2);
        assertNotNull("Seller was not created", sId3);

        String gotName = listingService.getSellerName(sId2);
        assertEquals("Got wrong seller name", "two", gotName);
        gotName = listingService.getSellerName(sId1);
        assertEquals("Got wrong seller name", "one", gotName);
        gotName = listingService.getSellerName(sId3);
        assertEquals("Got wrong seller name", "three", gotName);
    }

    @Test
    public void updateListing_fetchesUpdatedListing() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        assertNotNull("Seller was not created", sId1);
        assertNotNull("Seller was not created", sId2);
        assertNotNull("Seller was not created", sId3);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId3);
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);

        ListingObject l3 = new ListingObject("","titleNew", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId2);
        boolean success = listingService.updateListing(lId1, l3);
        assertTrue("Listing update unsuccessful", success);

        ListingObject updated1 = listingService.fetchListing(lId1);
        assertTrue("Did not update listing correctly", updated1.getTitle().equals("titleNew"));
    }

    @Test
    public void deleteListing_triesToFetchDeletedListing() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        assertNotNull("Seller was not created", sId1);
        assertNotNull("Seller was not created", sId2);
        assertNotNull("Seller was not created", sId3);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId3);
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);

        boolean success = listingService.deleteListing(lId1);
        assertTrue("Listing delete unsuccessful", success);

        ListingObject updated1 = listingService.fetchListing(lId1);
        assertNull("Did not delete listing correctly", updated1);
    }

    @Test
    public void getNextBidTotal_getsCorrectTotal() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        BuyerAccountObject bAccount1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String buyId1 = buyerModel.createNew(bAccount1);

        SellerAccountObject sAccount1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String selId1 = sellerModel.createNew(sAccount1);
        assertNotNull("Seller was not created", selId1);

        ListingObject listing1 = new ListingObject("","title", "description", "10.00", "5.00", "auctionStartDate", "auctionEndDate", "category", selId1);
        ListingObject listing2 = new ListingObject("","title", "description", "2.55", "2.25", "auctionStartDate", "auctionEndDate", "category", selId1);
        ListingObject listing3 = new ListingObject("","title", "description", "1.00", "1.10", "auctionStartDate", "auctionEndDate", "category", selId1);
        ListingObject listing4 = new ListingObject("","title", "description", "1.00", "1.00", "auctionStartDate", "auctionEndDate", "category", selId1);
        ListingObject listing5 = new ListingObject("","title", "description", "5.00", "5.00", "auctionStartDate", "auctionEndDate", "category", selId1);
        String lId1 = listingModel.createNew(listing1);
        String lId2 = listingModel.createNew(listing2);
        String lId3 = listingModel.createNew(listing3);
        String lId4 = listingModel.createNew(listing4);
        String lId5 = listingModel.createNew(listing5);
        listing1.setId(lId1);
        listing2.setId(lId2);
        listing3.setId(lId3);
        listing4.setId(lId4);
        listing5.setId(lId5);

        BidObject bid1 = new BidObject("", lId1, buyId1, "10.00");
        BidObject bid2 = new BidObject("", lId2, buyId1, "2.55");
        BidObject bid3 = new BidObject("", lId3, buyId1, "1.00");
        BidObject bid4 = new BidObject("", lId4, buyId1, "1.00");
        String bId1 = bidModel.createNew(bid1);
        String bId2 = bidModel.createNew(bid2);
        String bId3 = bidModel.createNew(bid3);
        String bId4 = bidModel.createNew(bid4);

        assertEquals("Got the wrong next bid total", "15.00", listingService.getNextBidTotal(listing1));
        assertEquals("Got the wrong next bid total", "4.80", listingService.getNextBidTotal(listing2));
        assertEquals("Got the wrong next bid total", "2.10", listingService.getNextBidTotal(listing3));
        assertEquals("Got the wrong next bid total", "2.00", listingService.getNextBidTotal(listing4));

        assertEquals("Got the wrong next bid total", "5.00", listingService.getNextBidTotal(listing5));
        BidObject bid5 = new BidObject("", lId5, buyId1, "6.00");
        String bId5 = bidModel.createNew(bid5);
        assertEquals("Got the wrong next bid total", "11.00", listingService.getNextBidTotal(listing5));
    }

    @Test
    public void fetchListing_fetchesCorrectListing() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        assertNotNull("Seller was not created", sId1);
        assertNotNull("Seller was not created", sId2);
        assertNotNull("Seller was not created", sId3);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId3);
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId1);
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", sId2);
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);
        assertNotNull("Listing was not created", lId1);
        assertNotNull("Listing was not created", lId2);
        assertNotNull("Listing was not created", lId3);

        ListingObject listing = listingService.fetchListing(lId2);
        assertEquals("Fetched wrong listing", l2.getTitle(), listing.getTitle());
        listing = listingService.fetchListing(lId1);
        assertEquals("Fetched wrong listing", l1.getTitle(), listing.getTitle());
        listing = listingService.fetchListing(lId3);
        assertEquals("Fetched wrong listing", l3.getTitle(), listing.getTitle());
    }

    @Test
    public void getNumBids_getsCorrectNumBids() {
        IListingModel listingModel = new ListingModelHSQLDB(dbUtil.getTestDbPath());
        ISellerModel sellerModel = new SellerModelHSQLDB(dbUtil.getTestDbPath());
        IBuyerModel buyerModel = new BuyerModelHSQLDB(dbUtil.getTestDbPath());
        IBidModel bidModel = new BidModelHSQLDB(dbUtil.getTestDbPath());
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        BuyerAccountObject bAccount1 = new BuyerAccountObject("ignored","name1", "other",
                "123 Someplace", "h0h 0h0","MB","email1", "pass1");
        String bId = buyerModel.createNew(bAccount1);

        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        assertNotNull("Seller was not created", sId1);
        assertNotNull("Seller was not created", sId2);
        assertNotNull("Seller was not created", sId3);

        ListingObject listing1 = new ListingObject("","title", "description", "10.00", "5.00", "auctionStartDate", "auctionEndDate", "category", sId1);
        ListingObject listing2 = new ListingObject("","title", "description", "2.55", "2.25", "auctionStartDate", "auctionEndDate", "category", sId1);
        ListingObject listing3 = new ListingObject("","title", "description", "1.00", "1.10", "auctionStartDate", "auctionEndDate", "category", sId1);
        String lId1 = listingModel.createNew(listing1);
        String lId2 = listingModel.createNew(listing2);
        String lId3 = listingModel.createNew(listing3);

        BidObject bid1 = new BidObject("0", lId1, bId, "10.00");
        BidObject bid2 = new BidObject("1", lId2, bId, "2.55");
        BidObject bid3 = new BidObject("0", lId1, bId, "1.00");
        BidObject bid4 = new BidObject("1", lId2, bId, "1.00");
        BidObject bid5 = new BidObject("0", lId1, bId, "1.00");
        String bId1 = bidModel.createNew(bid1);
        String bId2 = bidModel.createNew(bid2);
        String bId3 = bidModel.createNew(bid3);
        String bId4 = bidModel.createNew(bid4);
        String bId5 = bidModel.createNew(bid5);

        assertEquals("Got wrong number of bids", 2, listingService.getNumBids(lId2));
        assertEquals("Got wrong number of bids", 3, listingService.getNumBids(lId1));
        assertEquals("Got wrong number of bids", 0, listingService.getNumBids(lId3));
    }
}
