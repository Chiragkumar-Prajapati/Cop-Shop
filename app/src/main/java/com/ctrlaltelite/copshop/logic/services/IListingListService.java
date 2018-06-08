package com.ctrlaltelite.copshop.logic.services;

import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.ArrayList;

public interface IListingListService {
    /**
     * Fetch all listings
     * @return List of all unfiltered listings
     */
    ArrayList<ListingObject> fetchListings();

//    /**
//     * Fetch all listings, filtered
//     * @param filter ListFilter object to filter listings by
//     * @return Filtered list of all listings
//     */
//    ArrayList<ListingObject> fetchListings(ListFilter filter);
}