package com.ctrlaltelite.copshop.persistence.models;

import com.ctrlaltelite.copshop.logic.interfaces.IBuyerModel;
import com.ctrlaltelite.copshop.objects.BuyerAccountObject;
import com.ctrlaltelite.copshop.persistence.database.interfaces.IDatabase;

import java.util.ArrayList;
import java.util.Hashtable;

public class BuyerModel implements IBuyerModel {
    private static String TABLE_NAME = "Buyers";
    private IDatabase database;

    public BuyerModel(IDatabase database) {
        this.database = database;

        // Initialize mock data
    }

    @Override
    public String createNew(BuyerAccountObject newAccount) {
        Hashtable<String, String> newRow = new Hashtable<>();

        newRow.put("username", newAccount.getUsername());
        newRow.put("password", newAccount.getPassword());
        newRow.put("email", newAccount.getEmail());

        return this.database.insertRow(TABLE_NAME, newRow);
    }

    @Override
    public boolean update(String id, BuyerAccountObject updatedListing) {
        boolean success = true;
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "username", updatedListing.getUsername()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "password", updatedListing.getPassword()));
        success = success && (null != this.database.updateColumn(TABLE_NAME, id, "email", updatedListing.getEmail()));
        return success;
    }

    @Override
    public BuyerAccountObject fetch(String id) {
        BuyerAccountObject account = new BuyerAccountObject(
                id,
                this.database.fetchColumn(TABLE_NAME, id, "username"),
                this.database.fetchColumn(TABLE_NAME, id, "password"),
                this.database.fetchColumn(TABLE_NAME, id, "email")
        );

        return account;
    }

    @Override
    public BuyerAccountObject findByUsername(String username) {
        ArrayList<String> users = this.database.findByColumnValue(TABLE_NAME, "username", username);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            return this.fetch(id);
        }

        return null;
    }

    @Override
    public boolean checkUsernamePasswordMatch(String username, String inputPassword) {
        ArrayList<String> users = this.database.findByColumnValue(TABLE_NAME, "username", username);

        // We should only get back one user max, ignore extras
        if (!users.isEmpty()) {
            String id = users.get(0);
            String password = this.database.fetchColumn(TABLE_NAME, id, "password");
            return password.equals(inputPassword);
        }

        return false;
    }
}