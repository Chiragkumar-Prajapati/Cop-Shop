package com.ctrlaltelite.copshop.tests;

import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
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
        IListingService listingService = new ListingService(listingModel, sellerModel);

        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionStartTime", "auctionEndDate", "auctionEndTime", "3");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionStartTime", "auctionEndDate", "auctionEndTime", "1");
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionStartTime", "auctionEndDate", "auctionEndTime", "2");
        String lId1 = listingModel.createNew(l1);
        String lId2 = listingModel.createNew(l2);
        String lId3 = listingModel.createNew(l3);

        List listings = listingService.fetchListings();
        assertEquals("Did not fetch correct number of listings", 3, listings.size());
    }

    @Test
    public void getSellerNameFromListing_findsCorrectSeller() {
        IDatabase database = new MockDatabaseStub();
        ISellerModel sellerModel = new SellerModel(database);
        IListingModel listingModel = new ListingModel(database);
        IListingService listingService = new ListingService(listingModel, sellerModel);

        // Create the accounts
        SellerAccountObject a1 = new SellerAccountObject("", "pass", "email", "one");
        SellerAccountObject a2 = new SellerAccountObject("", "pass", "email", "two");
        SellerAccountObject a3 = new SellerAccountObject("", "pass", "email", "three");
        String sId1 = sellerModel.createNew(a1);
        String sId2 = sellerModel.createNew(a2);
        String sId3 = sellerModel.createNew(a3);
        // Create the listings
        ListingObject l1 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionStartTime", "auctionEndDate", "auctionEndTime", "2");
        ListingObject l2 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionStartTime", "auctionEndDate", "auctionEndTime", "0");
        ListingObject l3 = new ListingObject("","title", "description", "initPrice", "minBid", "auctionStartDate", "auctionStartTime", "auctionEndDate", "auctionEndTime", "1");
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
        IListingService listingService = new ListingService(listingModel, sellerModel);

        // Create the accounts
        SellerAccountObject a1 = new SellerAccountObject("", "pass", "email1", "one");
        SellerAccountObject a2 = new SellerAccountObject("", "pass", "email2", "two");
        SellerAccountObject a3 = new SellerAccountObject("", "pass", "email3", "three");
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
}
