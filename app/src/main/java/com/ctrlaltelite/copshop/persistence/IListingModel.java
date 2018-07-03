package com.ctrlaltelite.copshop.persistence;

import com.ctrlaltelite.copshop.objects.ListingObject;
import java.util.List;

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
     * Fetch listing objects by name from the database
     * @param name of the targeted listings
     * @return List containing all listings from DB with the given name as ListingObjectsListingObjects, or null
     */
    List<ListingObject> fetchByName(String name);

    /**
     * Fetch listing objects by sellerID from the database
     * @param sellerID of the targeted listings
     * @return List containing all listings from DB with the given sellerID as ListingObjectsListingObjects, or null
     */
    List<ListingObject> fetchBySellerID(String sellerID);

    /**
     * Fetch listing objects by category from the database
     * @param category of the targeted listings
     * @return List containing all listings from DB with the given category as ListingObjectsListingObjects, or null
     */
    List<ListingObject> fetchByCategory(String category);

    /**
     * Fetch listing objects by status from the database
     * @param status of the targeted listings
     * @return List containing all listings from DB with the given status as ListingObjectsListingObjects, or null
     */
    List<ListingObject> fetchByStatus(String status);

    /**
     * Fetch listing objects from the database that match all filters passed as parameters
     * @param name, location, category and status of the target listings
     * @return List containing all listings from DB with the given filters as ListingObjectsListingObjects, or null
     */
    List<ListingObject> fetchByFilters(String name, String location, String category, String status);

    /**
     * Fetches all listing objects from the database
     * @return List containing all listings as ListingObjects
     */
    List<ListingObject> fetchAll();

    /**
     * Get a count of all the various categories that exist in the database
     * @return int equal to the number of sellers in the database
     */
    int getNumCategories();

    /**
     * Get all the various categories of the listings
     * @return String[] array containing all the categories
     */
    String[] getAllCategories();
}
