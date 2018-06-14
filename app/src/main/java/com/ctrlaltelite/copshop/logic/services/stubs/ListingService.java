package com.ctrlaltelite.copshop.logic.services.stubs;

import android.support.annotation.NonNull;

import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.ISellerModel;

import java.util.ArrayList;

public class ListingService implements IListingService {
    private IListingModel listingModel;
    private ISellerModel sellerModel;

    public ListingService(IListingModel listingModel, ISellerModel sellerModel) {
        this.listingModel = listingModel;
        this.sellerModel = sellerModel;
    }

    public ArrayList<ListingObject> fetchListings() {
        return this.listingModel.fetchAll();
    }

//    public ArrayList<ListingObject> fetchListings(ListFilter filter) {
//        return filteredResults;
//    }

    @Override
    public String getSellerNameFromListing(String listingId) {
        ListingObject listing = listingModel.fetch(listingId);
        String sellerId = listing.getSellerId();

        return this.getSellerName(sellerId);
    }

    @Override
    public String getSellerName(String sellerId) {
        SellerAccountObject seller = sellerModel.fetch(sellerId);
        return seller.getOrganizationName();
    }

//    @Override
//    public String getNextBidTotal(ListingObject listing) {
//        return "1.00";
//    }

}
