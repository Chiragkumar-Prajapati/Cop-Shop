package com.ctrlaltelite.copshop.logic;

import android.app.Application;

import com.ctrlaltelite.copshop.logic.interfaces.IBuyerModel;
import com.ctrlaltelite.copshop.logic.interfaces.IListingModel;
import com.ctrlaltelite.copshop.logic.interfaces.ISellerModel;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;
import com.ctrlaltelite.copshop.persistence.models.BuyerModel;
import com.ctrlaltelite.copshop.persistence.models.ListingModel;
import com.ctrlaltelite.copshop.persistence.models.SellerModel;
import com.ctrlaltelite.copshop.persistence.database.stubs.MockDatabaseStub;

/**
 * Initializes singletons.
 * See manifest android:name - where it's initialized.
 * Data created here is not permanent, and can be destroyed at any time (typically when the app
 * enters the background). Data stored here is not a perfect replacement for a real database,
 * but should work for our stub database.
 */
public class CopShopApp extends Application {
    private static IDatabase database = new MockDatabaseStub();

    public static IBuyerModel buyerModel = new BuyerModel(database);
    public static ISellerModel sellerModel = new SellerModel(database);
    public static IListingModel listingModel = new ListingModel(database);
}
