package com.ctrlaltelite.copshop.persistence;

import com.ctrlaltelite.copshop.objects.BuyerAccountObject;

public interface IBuyerModel {
    /**
     * Create a new account from a BuyerAccountObject
     * @param newAccount The account object to store in the DB
     * @return String id of new account, or null
     */
    String createNew(BuyerAccountObject newAccount);

    /**
     * Update an account
     * @param id of the targeted account
     * @param updatedAccount The BuyerAccountObject to update the DB with
     * @return Success boolean
     */
    boolean update(String id, BuyerAccountObject updatedAccount);

    /**
     * Fetch a specific account's details by id
     * @param id of the targeted account
     * @return BuyerAccountObject from DB with the given id
     */
    BuyerAccountObject fetch(String id);

    /**
     * Fetch a specific account's details by their login info
     * @param username Their username
     * @return BuyerAccountObject with their details, or null
     */
    BuyerAccountObject findByEmail(String email);

    /**
     * Checks if a pair of credentials match a user in the DB
     * @param username Their username
     * @param password Their password
     * @return Boolean indicating if there was a credentials match or not
     */
    boolean checkEmailPasswordMatch(String email, String password);
}
