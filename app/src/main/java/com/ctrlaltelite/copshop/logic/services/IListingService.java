package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.ListingObject;
import java.util.List;

public interface IListingService {
    /**
     * Fetch all listings
     * @return List of all unfiltered listings
     */
    List<ListingObject> fetchListings();

//    /**
//     * Fetch all listings, filtered
//     * @param filter ListFilter object to filter listings by
//     * @return Filtered list of all listings
//     */
//    ArrayList<ListingObject> fetchListings(ListFilter filter);

    /**
     * Get the username of the seller who posted this listing
     * @param listingId Id of the listing
     * @return username of seller
     */
    String getSellerNameFromListing(String listingId);

    /**
     * Get the username of the seller who posted this listing
     * @param sellerId Id of the seller
     * @return username of seller
     */
    String getSellerName(String sellerId);

    /**
     * Get the next highest bid amount
     * @param listing the listing
     * @return String float representing next bid
     */
    String getNextBidTotal(ListingObject listing);
}