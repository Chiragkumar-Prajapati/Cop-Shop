package com.ctrlaltelite.copshop.tests.unit;

import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.BidModel;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;
import com.ctrlaltelite.copshop.persistence.stubs.SellerModel;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListingServiceTests {

    @Test
    public void fetchListings_fetchesAllListings() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "3");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "1");
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "2");
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);

        List listings = listingService.fetchListings();
        assertEquals("Did not fetch correct number of listings", 3, listings.size());
    }

    @Test
    public void fetchFilteredListings_fetchesFilteredListings() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        SellerAccountObject account1 = new SellerAccountObject("ignored", "name1", "123 Street", "A1A 1A1", "MB", "email1", "pass1");
        SellerAccountObject account2 = new SellerAccountObject("ignored", "name2", "123 Street", "A1A 1A1", "MB", "email2", "pass2");
        SellerAccountObject account3 = new SellerAccountObject("ignored", "name3", "123 Street", "A1A 1A1", "MB", "email3", "pass2");

        // Create the seller accounts
        String id1 = sellerModel.createNew(account1);
        String id2 = sellerModel.createNew(account2);
        String id3 = sellerModel.createNew(account3);

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
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the accounts
        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "2");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "1");
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
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the accounts
        SellerAccountObject a1 = new SellerAccountObject("", "one", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a2 = new SellerAccountObject("", "two", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        SellerAccountObject a3 = new SellerAccountObject("", "three", "123 Street", "A1A 1A1", "MB", "e@mail.com", "pass");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);

        String gotName = listingService.getSellerName(sId2);
        assertEquals("Got wrong seller name", "two", gotName);
        gotName = listingService.getSellerName(sId1);
        assertEquals("Got wrong seller name", "one", gotName);
        gotName = listingService.getSellerName(sId3);
        assertEquals("Got wrong seller name", "three", gotName);
    }

    @Test
    public void updateListing_fetchesUpdatedListing() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "3");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "1");
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);

        ListingObject l3 = new ListingObject("","titleNew", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "2");
        boolean success = listingService.updateListing(lId1, l3);
        assertTrue("Listing update unsuccessful", success);

        ListingObject updated1 = listingService.fetchListing(lId1);
        assertTrue("Did not update listing correctly", updated1.getTitle().equals("titleNew"));
    }

    @Test
    public void deleteListing_triesToFetchDeletedListing() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "3");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "1");
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);

        boolean success = listingService.deleteListing(lId1);
        assertTrue("Listing delete unsuccessful", success);

        ListingObject updated1 = listingService.fetchListing(lId1);
        assertNull("Did not delete listing correctly", updated1);
    }

    @Test
    public void getNextBidTotal_getsCorrectTotal() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        ListingObject listing1 = new ListingObject("0","title", "description", "10.00", "5.00", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject listing2 = new ListingObject("1","title", "description", "2.55", "2.25", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject listing3 = new ListingObject("2","title", "description", "1.00", "1.10", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject listing4 = new ListingObject("3","title", "description", "1.00", "1.00", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject listing5 = new ListingObject("4","title", "description", "5.00", "5.00", "auctionStartDate", "auctionEndDate", "category", "0");
        String lId1 = listingModel.createNew(listing1);
        String lId2 = listingModel.createNew(listing2);
        String lId3 = listingModel.createNew(listing3);
        String lId4 = listingModel.createNew(listing4);
        String lId5 = listingModel.createNew(listing5);

        BidObject bid1 = new BidObject("0", "0", "0", "10.00");
        BidObject bid2 = new BidObject("1", "1", "0", "2.55");
        BidObject bid3 = new BidObject("2", "2", "0", "1.00");
        BidObject bid4 = new BidObject("3", "3", "0", "1.00");
        String bId1 = bidModel.createNew(bid1);
        String bId2 = bidModel.createNew(bid2);
        String bId3 = bidModel.createNew(bid3);
        String bId4 = bidModel.createNew(bid4);

        assertEquals("Got the wrong next bid total", "15.00", listingService.getNextBidTotal(listing1));
        assertEquals("Got the wrong next bid total", "4.80", listingService.getNextBidTotal(listing2));
        assertEquals("Got the wrong next bid total", "2.10", listingService.getNextBidTotal(listing3));
        assertEquals("Got the wrong next bid total", "2.00", listingService.getNextBidTotal(listing4));

        assertEquals("Got the wrong next bid total", "5.00", listingService.getNextBidTotal(listing5));
        BidObject bid5 = new BidObject("4", "4", "0", "6.00");
        String bId5 = bidModel.createNew(bid5);
        assertEquals("Got the wrong next bid total", "11.00", listingService.getNextBidTotal(listing5));
    }

    @Test
    public void fetchListing_fetchesCorrectListing() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "3");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "1");
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionEndDate", "category", "2");
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);

        ListingObject listing = listingService.fetchListing(lId2);
        assertEquals("Fetched wrong listing", l2.getTitle(), listing.getTitle());
        listing = listingService.fetchListing(lId1);
        assertEquals("Fetched wrong listing", l1.getTitle(), listing.getTitle());
        listing = listingService.fetchListing(lId3);
        assertEquals("Fetched wrong listing", l3.getTitle(), listing.getTitle());
    }

    @Test
    public void getNumBids_getsCorrectNumBids() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IBidModel bidModel = new BidModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel, bidModel);

        ListingObject listing1 = new ListingObject("0","title", "description", "10.00", "5.00", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject listing2 = new ListingObject("1","title", "description", "2.55", "2.25", "auctionStartDate", "auctionEndDate", "category", "0");
        ListingObject listing3 = new ListingObject("2","title", "description", "1.00", "1.10", "auctionStartDate", "auctionEndDate", "category", "0");
        String lId1 = listingModel.createNew(listing1);
        String lId2 = listingModel.createNew(listing2);
        String lId3 = listingModel.createNew(listing3);

        BidObject bid1 = new BidObject("0", "0", "0", "10.00");
        BidObject bid2 = new BidObject("1", "1", "0", "2.55");
        BidObject bid3 = new BidObject("0", "0", "0", "1.00");
        BidObject bid4 = new BidObject("1", "1", "0", "1.00");
        BidObject bid5 = new BidObject("0", "0", "0", "1.00");
        String bId1 = bidModel.createNew(bid1);
        String bId2 = bidModel.createNew(bid2);
        String bId3 = bidModel.createNew(bid3);
        String bId4 = bidModel.createNew(bid4);
        String bId5 = bidModel.createNew(bid5);

        assertEquals("Got wrong number of bids", 2, listingService.getNumBids("1"));
        assertEquals("Got wrong number of bids", 3, listingService.getNumBids("0"));
        assertEquals("Got wrong number of bids", 0, listingService.getNumBids("2"));
    }
}
