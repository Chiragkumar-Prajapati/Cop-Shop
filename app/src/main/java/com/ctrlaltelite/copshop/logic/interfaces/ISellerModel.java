package com.ctrlaltelite.copshop.logic.interfaces;

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
     * @param updatedListing The SellerAccountObject to update the DB with
     * @return Success boolean
     */
    boolean update(String id, SellerAccountObject updatedListing);

    /**
     * Fetch a specific account's details by id
     * @param id of the targeted account
     * @return SellerAccountObject from DB with the given id
     */
    SellerAccountObject fetch(String id);

    /**
     * Fetch a specific account's details by their login info
     * @param username Their username
     * @return BuyerAccountObject with their details, or null
     */
    SellerAccountObject findByUsername(String username);

    /**
     * Checks if a pair of credentials match a user in the DB
     * @param username Their username
     * @param password Their password
     * @return Boolean indicating if there was a credentials match or not
     */
    boolean checkUsernamePasswordMatch(String username, String password);
}
