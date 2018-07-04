package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.application.CopShopHub;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.SellerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.logic.services.IBidService;
import com.ctrlaltelite.copshop.persistence.IListingModel;
import com.ctrlaltelite.copshop.logic.services.IListingService;
import com.ctrlaltelite.copshop.objects.ListingObject;
import com.ctrlaltelite.copshop.persistence.ISellerModel;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ListingService implements IListingService {
    private IListingModel listingModel;
    private ISellerModel sellerModel;
    private IBidModel bidModel;

    public ListingService(IListingModel listingModel, ISellerModel sellerModel, IBidModel bidModel) {
        this.listingModel = listingModel;
        this.sellerModel = sellerModel;
        this.bidModel = bidModel;
    }

    public List<ListingObject> fetchListings() {
        return this.listingModel.fetchAll();
    }

    @Override
    public List<ListingObject> fetchListingsByFilters(String name, String location, String category, String status) {
        // get seller ID
        String sellerID = sellerModel.getIdFromName(location);

        return this.listingModel.fetchByFilters(name, sellerID, category, status);
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

    @Override
    public String getNextBidTotal(ListingObject listing) {
        List<BidObject> bids = bidModel.findAllByListing(listing.getId());
        Float minBid = Float.parseFloat(listing.getMinBid());
        Float initPrice = Float.parseFloat(listing.getInitPrice());
        Float bidAmount;

        if (bids.size() > 0) {
            Collections.sort(bids);
            BidObject highestBid = bids.get(0);
            Float highestBidAmt = Float.parseFloat(highestBid.getBidAmt());

            bidAmount = highestBidAmt + minBid;
        } else {
             bidAmount = initPrice;
        }

        return String.format(Locale.CANADA, "%1$.2f", bidAmount);
    }

    @Override
    public ListingObject fetchListing(String listingId) {
        if (null == listingId) { throw new IllegalArgumentException("listingId cannot be null"); }

        return listingModel.fetch(listingId);
    }

    @Override
    public boolean deleteListing(String listingId) {
        if (null == listingId) { throw new IllegalArgumentException("listingId cannot be null"); }

        List<BidObject> bids = CopShopHub.getBidService().fetchBidsForListing(listingId);
        boolean success = true;

        for (BidObject bid : bids) {
            success = success && bidModel.delete(bid.getId());
        }

        if (success) {
            success = listingModel.delete(listingId);
        }
        return success;
    }

    @Override
    public boolean updateListing(String listingId, ListingObject updatedObj) {
        if (null == listingId) {
            throw new IllegalArgumentException("listingId cannot be null");
        }
        if (null == updatedObj) {
            throw new IllegalArgumentException("updated object cannot be null");
        }

        return listingModel.update(listingId, updatedObj);
    }

    @Override
    public int getNumBids(String listingId) {
        List<BidObject> bids = bidModel.findAllByListing(listingId);
        return bids.size();
    }
}
