package com.ctrlaltelite.copshop.logic;

import android.app.Application;

import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.IListingListService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingListService;
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
    public static IListingListService listingListService = new ListingListService(listingModel);
    public static IAccountService accountService = new AccountService(sellerModel, buyerModel);
    public static ICreateListingService createListingService = new CreateListingService(listingModel);

    public CopShopApp() {
        super();

        // Initialize tables with dummy data
        // Listings
        ListingObject newListing1 = new ListingObject("", "Sample 1", "This is a sample description that describes this sample listing", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing2 = new ListingObject("", "Sample 2", "This is a description that is a sample to describe this listing", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing3 = new ListingObject("", "Sample 3", "This is a listing sample with a sample description", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing4 = new ListingObject("", "Sample 4", "This is a description for a sample listing, this description is a very long description and should be truncated with ellipsis.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing5 = new ListingObject("", "Sample 5", "This is a sample description that describes this sample listing", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing6 = new ListingObject("", "Sample 6", "This is a description that is a sample to describe this listing", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing7 = new ListingObject("", "Sample 7", "This is a listing sample with a sample description", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");
        ListingObject newListing8 = new ListingObject("", "Sample 8", "This is a description for a sample listing, this description is a very long description and should be truncated with ellipsis.", "10.00", "1.00", "01/02/2018", "12:00", "10/02/2018", "20:00", "1");

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
    }
}
