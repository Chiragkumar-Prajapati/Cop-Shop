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
     * Fetch a listing from the db by listingId
     * @param listingId The listing id
     * @return ListingObject
     */
    ListingObject fetchListing(String listingId);

    /**
     * Delete a listing from the db by listingId. Starts by deleting
     * all associated bids.
     * @param listingId The listing id
     * @return success or not
     */
    boolean deleteListing(String listingId);

    /**
     * Update a listing in the db by listingId
     * @param listingId The listing id
     * @param updatedObj The new version of the listing
     * @return success or not
     */
    boolean updateListing(String listingId, ListingObject updatedObj);

    /**
     * Get the next highest bid amount
     * @param listing the listing
     * @return String float representing next bid
     */
    String getNextBidTotal(ListingObject listing);

    /**
     * Get number of bids on a listing
     * @param listingId The listing to count bids for
     * @return Int number of bids
     */
    int getNumBids(String listingId);

}