package com.ctrlaltelite.copshop.logic;

import android.app.Application;

import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.stubs.BuyerModel;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;
import com.ctrlaltelite.copshop.persistence.stubs.SellerModel;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;

/**
 * Initializes singletons.
 * See manifest android:name - where it's initialized.
 * Data created here is not permanent, and can be destroyed at any time (typically when the app
 * enters the background). Data stored here is not a perfect replacement for a real database,
 * but should work for our stub database.
 */
public class CopShopApp extends Application {
    // Database
    private static IDatabase database = new MockDatabaseStub();
    // Database models
    public static IBuyerModel buyerModel = new BuyerModel(database);
    public static ISellerModel sellerModel = new SellerModel(database);
    public static IListingModel listingModel = new ListingModel(database);
    // Services
    public static IListingService listingService = new ListingService(listingModel, sellerModel);
    public static IAccountService accountService = new AccountService(sellerModel, buyerModel);
    public static ICreateListingService createListingService = new CreateListingService(listingModel);

    public CopShopApp() {
        super();
        // Initialize tables with dummy data
        // Listings
        ListingObject newListing1 = new ListingObject("", "Bicycle 1", "Poor condition, was found stuck in a tree upon day of seizure.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing2 = new ListingObject("", "Nondescript Firearm 2", "It's a gun. We think it shoots bullets, maybe nerf darts. Hard to say.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing3 = new ListingObject("", "Old Hotdog 3", "Still smells alright.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing4 = new ListingObject("", "Toothbrush 4", "Pre-lubricated. The handle is filed to a sharp point to spare you expense in toothpicks. Has probably never been used before.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing5 = new ListingObject("", "Several Gerbils 5", "Assorted colors. You might need a net to come pick them up.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing6 = new ListingObject("", "Pen 6", "Blue ink. It doesn't write very well.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing7 = new ListingObject("", "Live Octopus 7", "It keeps oozing out of its tank and escaping. Please take it away.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing8 = new ListingObject("", "Riding Lawnmower 8", "Front tire is flat and needs to be replaced. Doesn't cut grass evenly. Has a couple of stains and dents. You need a license to drive this machine.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        listingModel.createNew(newListing1);
        listingModel.createNew(newListing2);
        listingModel.createNew(newListing3);
        listingModel.createNew(newListing4);
        listingModel.createNew(newListing5);
        listingModel.createNew(newListing6);
        listingModel.createNew(newListing7);
        listingModel.createNew(newListing8);

        // Buyer accounts
        BuyerAccountObject newBuyerAccount1 = new BuyerAccountObject("", "John", "Smith", "123 Street", "A1A 1A1", "MB", "john@email.com", "john");
        buyerModel.createNew(newBuyerAccount1);

        // Seller accounts
        SellerAccountObject newSeller = new SellerAccountObject("1", "Local Precinct", "12345", "local@police.com");
        sellerModel.createNew(newSeller);
    }
}
