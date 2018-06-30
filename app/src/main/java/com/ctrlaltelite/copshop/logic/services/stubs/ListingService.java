package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.ISellerModel;

import java.util.List;

public class ListingService implements IListingService {
    private IListingModel listingModel;
    private ISellerModel sellerModel;

    public ListingService(IListingModel listingModel, ISellerModel sellerModel) {
        this.listingModel = listingModel;
        this.sellerModel = sellerModel;
    }

    public List<ListingObject> fetchListings() {
        return this.listingModel.fetchAll();
    }

//    public ArrayList<ListingObject> fetchListings(ListFilter filter) {
//        return filteredResults;
//    }

    @Override
    public String getSellerNameFromListing(String listingId) {
        if (null == listingId) { throw new IllegalArgumentException("listingId cannot be null"); }

        ListingObject listing = listingModel.fetch(listingId);
        String sellerId = listing.getSellerId();

        return this.getSellerName(sellerId);
    }

    @Override
    public String getSellerName(String sellerId) {
        if (null == sellerId) { throw new IllegalArgumentException("sellerId cannot be null"); }

        SellerAccountObject seller = sellerModel.fetch(sellerId);
        return seller.getOrganizationName();
    }

//    @Override
//    public String getNextBidTotal(ListingObject listing) {
//        return "1.00";
//    }

    @Override
    public ListingObject fetchListing(String listingId) {
        if (null == listingId) { throw new IllegalArgumentException("listingId cannot be null"); }

        return listingModel.fetch(listingId);
    }
}
