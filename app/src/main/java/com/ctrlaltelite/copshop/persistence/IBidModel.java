package com.ctrlaltelite.copshop.persistence;

import com.ctrlaltelite.copshop.objects.BidObject;

import java.util.List;

public interface IBidModel {
    /**
     * Create a new bid
     * @param newBid BidObject for the new bid
     * @return id of the new bid
     */
    String createNew(BidObject newBid);

    /**
     * Fetch a specific bid by id
     * @param id of the targeted bid
     * @return BidObject from DB with the given id
     */
    BidObject fetch(String id);

    /**
     * Find all bids by listing id
     * @param listingId Listing id
     * @return List of BidObjects belonging to given listing
     */
    List<BidObject> findAllByListing(String listingId);
}
