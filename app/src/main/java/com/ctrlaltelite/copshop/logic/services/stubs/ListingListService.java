package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.logic.services.IListingListService;
import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.ArrayList;

public class ListingListService implements IListingListService {
    private IListingModel listingModel;

    public ListingListService(IListingModel listingModel) {
        this.listingModel = listingModel;
    }

    public ArrayList<ListingObject> fetchListings() {
        return this.listingModel.fetchAll();
    }

//    public ArrayList<ListingObject> fetchListings(ListFilter filter) {
//        return filteredResults;
//    }

}
