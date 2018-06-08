package com.ctrlaltelite.copshop.persistence;

import com.ctrlaltelite.copshop.objects.ListingObject;

import java.util.ArrayList;

public interface IListingModel {
    /**
     * Create a new listing from a ListingObject
     * @param newListing The listing object to store in the DB
     * @return String id of the new listing, or null
     */
    String createNew(ListingObject newListing);

    /**
     * Delete a listing
     * @param id of the targeted listing
     * @return Success boolean
     */
    boolean delete(String id);

    /**
     * Update a listing
     * @param id of the targeted listing
     * @param updatedListing The ListingObject to update the DB's listing with
     * @return Success boolean
     */
    boolean update(String id, ListingObject updatedListing);

    /**
     * Fetch a specific listing by id
     * @param id of the targeted listing
     * @return ListingObject from DB with the given id, or null
     */
    ListingObject fetch(String id);

    /**
     * Fetches all listing objects from the database
     * @return ArrayList containing all listings as ListingObjects
     */
    ArrayList<ListingObject> fetchAll();
}
