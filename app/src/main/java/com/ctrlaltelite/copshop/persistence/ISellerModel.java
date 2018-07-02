package com.ctrlaltelite.copshop.persistence;

import com.ctrlaltelite.copshop.objects.SellerAccountObject;

public interface ISellerModel {
    /**
     * Create a new account from a SellerAccountObject
     * @param newAccount The account object to store in the DB
     * @return String id of new account, or null
     */
    String createNew(SellerAccountObject newAccount);

    /**
     * Update an account
     * @param id of the targeted account
     * @param updatedAccount The SellerAccountObject to update the DB with
     * @return Success boolean
     */
    boolean update(String id, SellerAccountObject updatedAccount);

    /**
     * Fetch a specific account's details by id
     * @param id of the targeted account
     * @return SellerAccountObject from DB with the given id
     */
    SellerAccountObject fetch(String id);

    /**
     * Fetch a specific account's details by their login info
     * @param email Their email
     * @return SellerAccountObject with their details, or null
     */
    SellerAccountObject findByEmail(String email);

    /**
     * Checks if a pair of credentials match a user in the DB
     * @param email Their email
     * @param password Their password
     * @return Boolean indicating if there was a credentials match or not
     */
    boolean checkEmailPasswordMatch(String email, String password);

    /**
     * Returns the id in the database of the sellerName passed as parameter
     * @param sellerName whose id to find
     * @return the id of the seller
     */
    String getSellerID(String sellerName);

    /**
     * Get a count of all sellers that exist in the database
     * @return int equal to the number of sellers in the database
     */
    int getNumSellers();

    /**
     * Get all the names of the sellers(locations)
     * @return String[] array containing all the (sellers/locations)' names
     */
    String[] getAllSellerNames();
}
