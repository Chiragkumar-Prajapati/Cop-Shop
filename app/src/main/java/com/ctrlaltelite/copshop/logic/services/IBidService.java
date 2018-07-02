package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.BidObject;

import java.util.List;

public interface IBidService {

    /**
     * Fetch all bids that belong to a listing
     * @param listingId The listing id
     * @return List of BidObjects for the given listing
     */
    List<BidObject> fetchBidsForListing(String listingId);

    /**
     * Create a bid for a listing
     * @param suggestedBid The amount suggested for the bid - minimum amount biddable
     * @param listingId The listing this bid belongs to
     * @param bidAmount The bid amount requested
     * @param buyerId the buyer this bid belongs to
     * @return True if the bid was placed, false if an error occurred
     */
    boolean createBid(String suggestedBid, String listingId, String bidAmount, String buyerId);

}
