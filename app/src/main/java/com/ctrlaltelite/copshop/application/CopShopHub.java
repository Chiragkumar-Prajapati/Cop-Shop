package com.ctrlaltelite.copshop.application;

import com.ctrlaltelite.copshop.logic.services.IAccountService;
import com.ctrlaltelite.copshop.logic.services.IBidService;
import com.ctrlaltelite.copshop.logic.services.ICreateListingService;
import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.AccountService;
import com.ctrlaltelite.copshop.logic.services.stubs.BidService;
import com.ctrlaltelite.copshop.logic.services.stubs.CreateListingService;
import com.ctrlaltelite.copshop.logic.services.stubs.ListingService;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.persistence.ISellerModel;
import com.ctrlaltelite.copshop.persistence.database.IDatabase;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;
import com.ctrlaltelite.copshop.persistence.stubs.BidModel;
import com.ctrlaltelite.copshop.persistence.stubs.ListingModel;
import com.ctrlaltelite.copshop.persistence.stubs.SellerModel;
import com.ctrlaltelite.copshop.persistence.stubs.BuyerModel;
import com.ctrlaltelite.copshop.persistence.stubs.hsqldb.BidModelHSQLDB;
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
    private static IBidModel bidModel = null;
    // Services
    private static IListingService listingService = null;
    private static IAccountService accountService = null;
    private static ICreateListingService createListingService = null;
    private static IBidService bidService = null;
    private static IUserSession userSessionService = null;

    public static synchronized IListingService getListingService()
    {
        if (null == listingService) {
            listingService = new ListingService(getListingModel(), getSellerModel(), getBidModel());
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

    public static synchronized IBidService getBidService()
    {
        if (null == bidService) {
            bidService = new BidService(getBidModel(), getBuyerModel(), getListingModel());
        }
        return bidService;
    }

    public static synchronized IUserSession getUserSessionService()
    {
        if (null == userSessionService){
            userSessionService = new UserSession();
        }
        return userSessionService;
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
                BuyerAccountObject newBuyerAccount2 = new BuyerAccountObject("", "Fernando", "Fernandez", "7 Gordo Road", "7G7 5D2", "MB", "fernando@rulez.com", "frontalLobotomy");
                buyerModel.createNew(newBuyerAccount2);
                BuyerAccountObject newBuyerAccount3 = new BuyerAccountObject("", "Naomi", "Nondescript", "11 Generic Bay", "1A2 B3C", "MB", "email@internet.com", "default");
                buyerModel.createNew(newBuyerAccount3);

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
                SellerAccountObject newSeller2 = new SellerAccountObject("", "International Precinct",  "99 Brooklyn", "R2R3H3", "Manitoba", "distant@police.com", "12345");
                sellerModel.createNew(newSeller2);
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
                ListingObject newListing1 = new ListingObject("", "Bicycle 1", "Poor condition, was found stuck in a tree upon day of seizure.", "10.00", "5.00", "01/02/2018 12:00", "10/02/2019 20:00", "Bikes", "","0");
                ListingObject newListing2 = new ListingObject("", "Nondescript Firearm 2", "It's a gun. We think it shoots bullets, maybe nerf darts. Hard to say.", "10.00", "1.00", "01/02/2018 12:00", "10/02/2018 20:00", "Weapons", "", "1");
                ListingObject newListing3 = new ListingObject("", "Old Hotdog 3", "Still smells alright.", "10.00", "1.00", "01/02/2018 12:00", "10/02/2020 20:00", "Food", "", "1");
                ListingObject newListing4 = new ListingObject("", "Toothbrush 4", "Pre-lubricated. The handle is filed to a sharp point to spare you expense in toothpicks. Has probably never been used before.", "10.00", "1.00", "01/02/2018 12:00", "10/02/2018 20:00","Oral Hygiene Instrument" ,"", "0");
                ListingObject newListing5 = new ListingObject("", "Several Gerbils 5", "Assorted colors. You might need a net to come pick them up.", "10.00", "1.00", "01/02/2018 12:00", "10/02/2018 20:00", "Pets", "", "0");
                ListingObject newListing6 = new ListingObject("", "Pen 6", "Blue ink. It doesn't write very well.", "10.00", "1.00", "01/02/2018 12:00", "12/12/2018 20:00", "Writing Equipment","1", "");
                ListingObject newListing7 = new ListingObject("", "Live Octopus 7", "It keeps oozing out of its tank and escaping. Please take it away.", "10.00", "1.00", "01/02/2018 12:00", "10/02/2018 20:00", "Pets", "", "0");
                ListingObject newListing8 = new ListingObject("", "Riding Lawnmower 8", "Front tire is flat and needs to be replaced. Doesn't cut grass evenly. Has a couple of stains and dents. You need a license to drive this machine.", "10.00", "1.00", "01/02/2018 12:00", "10/02/2018 20:00", "Hot Rides","", "0");
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

    public static synchronized IBidModel getBidModel()
    {
        if (null == bidModel) {
            if (USE_REAL_DB) {
                bidModel = new BidModelHSQLDB(getDBPath());
            } else {
                bidModel = new BidModel(getDatabase());
                // Add sample data
                BidObject newBid1 = new BidObject("","0","0","10.00");
                BidObject newBid2 = new BidObject("","0","1","15.00");
                BidObject newBid3 = new BidObject("","0","2","20.00");
                BidObject newBid4 = new BidObject("","0","0","25.00");
                bidModel.createNew(newBid1);
                bidModel.createNew(newBid2);
                bidModel.createNew(newBid3);
                bidModel.createNew(newBid4);
            }
        }
        return bidModel;
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
