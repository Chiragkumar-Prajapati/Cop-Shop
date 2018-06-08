package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.logic.CopShopApp;
import com.ctrlaltelite.copshop.logic.services.IListingListService;
import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.ArrayList;

public class ListingListService implements IListingListService {

    public ArrayList<ListingObject> fetchListings() {
        return CopShopApp.listingModel.fetchAll();
    }

//    public ArrayList<ListingObject> fetchListings(ListFilter filter) {
//        return filteredResults;
//    }

}
