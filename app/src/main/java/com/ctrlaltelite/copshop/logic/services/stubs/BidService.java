package com.ctrlaltelite.copshop.logic.services.stubs;

import com.ctrlaltelite.copshop.logic.services.IBidService;
import com.ctrlaltelite.copshop.objects.BidObject;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.persistence.IBidModel;
import com.ctrlaltelite.copshop.persistence.IBuyerModel;
import com.ctrlaltelite.copshop.persistence.IListingModel;

import java.util.Collections;
import java.util.List;

public class BidService implements IBidService {
    private IBidModel bidModel;
    private IBuyerModel buyerModel;
    private IListingModel listingModel;

    public BidService(IBidModel bidModel, IBuyerModel buyerModel, IListingModel listingModel) {
        this.bidModel = bidModel;
        this.buyerModel = buyerModel;
        this.listingModel = listingModel;
    }

    @Override
    public List<BidObject> fetchBidsForListing(String listingId) {
        List<BidObject> bids = bidModel.findAllByListing(listingId);
        Collections.sort(bids);
        return bids;
    }

    @Override
    public boolean createBid(String suggestedBid, String listingId, String bidAmount, String buyerId) {
        Float floatBidAmount = Float.parseFloat(bidAmount);
        Float floatSuggestedAmount = Float.parseFloat(suggestedBid);

        if (floatBidAmount >= floatSuggestedAmount) {
            BidObject bid = new BidObject("", listingId, buyerId, bidAmount );
            return null != bidModel.createNew(bid);
        } else {
            return false;
        }
    }
}
