package com.ctrlaltelite.copshop.application;

import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;
import com.ctrlaltelite.copshop.persistence.stubs.SellerModel;
import com.ctrlaltelite.copshop.persistence.stubs.BuyerModel;
import com.ctrlaltelite.copshop.persistence.stubs.hsqldb.BuyerModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.stubs.hsqldb.ListingModelHSQLDB;
import com.ctrlaltelite.copshop.persistence.stubs.hsqldb.SellerModelHSQLDB;

/**
 * Central location for instantiating and distributing Service and database Model singletons
 */
public class CopShopHub
{
    private static boolean USE_REAL_DB = true;
    // Database setup
    private static String dbPath = "copshopdb";
    private static IDatabase database = null;
    // DB Models
    private static IBuyerModel buyerModel = null;
    private static ISellerModel sellerModel = null;
    private static IListingModel listingModel = null;
    // Services
    private static IListingService listingService = null;
    private static IAccountService accountService = null;
    private static ICreateListingService createListingService = null;

    public static synchronized IListingService getListingService()
    {
        if (null == listingService) {
            listingService = new ListingService(getListingModel(), getSellerModel());
        }
        return listingService;
    }

    public static synchronized IAccountService getAccountService()
    {
        if (null == accountService) {
            accountService = new AccountService(getSellerModel(), getBuyerModel());
        }
        return accountService;
    }

    public static synchronized ICreateListingService getCreateListingService()
    {
        if (null == createListingService) {
            createListingService = new CreateListingService(getListingModel());
        }
        return createListingService;
    }

	public static synchronized IBuyerModel getBuyerModel()
    {
		if (null == buyerModel) {
		    if (USE_REAL_DB) {
                buyerModel = new BuyerModelHSQLDB(getDBPath());
            } else {
                buyerModel = new BuyerModel(getDatabase());
                // Add sample data
                BuyerAccountObject newBuyerAccount1 = new BuyerAccountObject("", "John", "Smith", "123 Street", "A1A 1A1", "MB", "john@email.com", "john");
                buyerModel.createNew(newBuyerAccount1);

            }
        }
        return buyerModel;
	}

    public static synchronized ISellerModel getSellerModel()
    {
        if (null == sellerModel) {
            if (USE_REAL_DB) {
                sellerModel = new SellerModelHSQLDB(getDBPath());
            } else {
                sellerModel = new SellerModel(getDatabase());
                // Add sample data
                SellerAccountObject newSeller = new SellerAccountObject("", "Local Precinct",  "60 Police St", "R2R3H3", "Manitoba", "local@police.com", "12345");
                sellerModel.createNew(newSeller);

            }
        }
        return sellerModel;
    }

    public static synchronized IListingModel getListingModel()
    {
        if (null == listingModel) {
            if (USE_REAL_DB) {
                listingModel = new ListingModelHSQLDB(getDBPath());
            } else {
                listingModel = new ListingModel(getDatabase());
                // Add sample data
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
            }
        }
        return listingModel;
    }

    private static synchronized IDatabase getDatabase() {
        if (null == database) {
            database = new MockDatabaseStub();
        }
        return database;
    }

    public static void setDBPath(final String path) {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not set database driver: " + e);
        }
        dbPath = path;
    }

    public static String getDBPath() {
        return dbPath;
    }
}
